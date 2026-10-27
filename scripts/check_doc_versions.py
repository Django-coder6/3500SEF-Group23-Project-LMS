#!/usr/bin/env python3
"""Check that a document's version header agrees with its own revision history.

Why this exists
---------------
Four times now a document in this repository has declared one version in its
header table and listed a newer one at the bottom of its revision history:

    glossary.md        header v1.0, history reached v1.1
    change-log.md      header v1.0, history reached v1.1
    risk-register.md   header v1.0, history reached v1.2
    change-log.md      header v1.1, history reached v1.2

It is the same root cause CR-012 records: one fact written in two places drifts.
The fix is not to remember harder, it is to check it.

What it looks for
-----------------
A versioned document carries two things:

    | Version | v1.2 |                     <- the header declaration

    ## Revision history
    | Version | Date | Change | Author |
    |---|---|---|---|
    | v1.0 | Week 1 | ... | R1 |         <- the revision history
    | v1.2 | Week 3 | ... | R1 |

If both exist, the header version must equal the last revision row, and no
revision label may appear twice.

A document that is not on the REQUIRED list is left alone when it carries no
header, because a design note or a logbook has no need of one. A document that
**is** on the list is reported when a part is missing: silence there would be read
as coverage, and six documents once sat in exactly that silence while the exit code
said all agree.

Usage
-----
    python3 scripts/check_doc_versions.py            # whole repository
    python3 scripts/check_doc_versions.py docs/      # one subtree

Exit code 0 when every versioned document agrees, 1 otherwise.
"""
import os
import re
import sys

# The version may carry a parenthetical after it, as SRS.md and test-plan.md do.
# The old pattern required a pipe straight after the number, so every document that
# wrote "v0.5 (draft, Chapters 1 to 4)" was silently skipped.
HEADER = re.compile(r'^\|\s*Version\s*\|\s*(v[\d.]+)\b', re.M)
REVISION_HEADING = re.compile(r'^#{1,4}\s*Revision history\s*$', re.M | re.I)
REVISION_ROW = re.compile(r'^\|\s*(v[\d.]+)\s*\|', re.M)

# Documents that must carry a version header and a revision history. A file listed
# here is reported when either part is missing, so the check cannot pass by
# skipping it. Add a path here when a document starts making versioned promises.
REQUIRED = {
    'docs/01-requirements/specification/SRS.md',
    'docs/01-requirements/specification/product-backlog.md',
    'docs/01-requirements/elicitation/Personas.md',
    'docs/01-requirements/elicitation/survey-results.md',
    'docs/01-requirements/elicitation/user-journey-maps.md',
    'docs/02-design/database/ERD.md',
    'docs/04-testing/test-plan.md',
    'docs/02-design/adr/ADR-004-location-recommendation.md',
    'docs/05-management/change-log.md',
    'docs/05-management/risk-register.md',
    'docs/05-management/plans/PLAN.md',
    'docs/06-logbook/README.md',
    'docs/02-design/backend-state-machines.md',
    'docs/02-design/design-system.md',
    'docs/02-design/frontend-conventions.md',
    'docs/02-design/frontend-open-items.md',
    'docs/02-design/frontend-page-map.md',
    'docs/02-design/location-recommender-rules.md',
    'docs/02-design/overdue-fee-rules.md',
    'docs/02-design/ui-prototype.md',
    'docs/03-api/frontend-api-dependencies.md',
    'docs/04-testing/test-cases/README.md',
    'docs/07-report/chapters/00-outline.md',
}

# The design and API baseline. These documents had no version header until week 3, so the check
# skipped them silently while Sprint 2 built on top of them.
DESIGN_DOCUMENTS = {
    'docs/02-design/backend-state-machines.md',
    'docs/02-design/design-system.md',
    'docs/02-design/frontend-conventions.md',
    'docs/02-design/frontend-page-map.md',
    'docs/02-design/ui-prototype.md',
    'docs/02-design/frontend-open-items.md',
    'docs/02-design/location-recommender-rules.md',
    'docs/02-design/overdue-fee-rules.md',
    'docs/03-api/frontend-api-dependencies.md',
    'docs/04-testing/test-cases/README.md',
    'docs/07-report/chapters/00-outline.md',
}

# YAML documents are checked against their own `info.version` field and a comment block, because
# they cannot carry a markdown table. Only the contract is here; it is the one a change to which
# has to be visible.
REQUIRED_YAML = {'docs/03-api/openapi.yaml'}

SKIP_DIRS = {'.git', 'node_modules', 'dist', 'build', 'target', '.github'}


def find_markdown(root):
    if os.path.isfile(root):
        yield root
        return
    for dirpath, dirnames, filenames in os.walk(root):
        dirnames[:] = [d for d in dirnames if d not in SKIP_DIRS]
        for name in sorted(filenames):
            if name.endswith('.md'):
                yield os.path.join(dirpath, name)


def display_path(path):
    return path.replace(os.sep, '/').lstrip('./')


def check(path):
    """Return (header, last, rows) or None when the file is not versioned.

    A file on the REQUIRED list never returns None: a missing part is reported as a
    problem by the caller, using the tuple below with None for the part that is absent.
    """
    with open(path, encoding='utf-8') as fh:
        text = fh.read()

    header = HEADER.search(text)
    if not header:
        return None

    heading = REVISION_HEADING.search(text)
    if not heading:
        # A header without a revision history is not this script's business.
        return None

    body = text[heading.end():]
    rows = [m.group(1) for m in REVISION_ROW.finditer(body)]
    if not rows:
        return None

    return header.group(1), rows[-1], rows



YAML_VERSION = re.compile(r'^\s{2}version:\s*([\d.]+)', re.M)
YAML_HIST = re.compile(r'^#\s*Revision history\s*$', re.M)


def find_yaml(root):
    if os.path.isfile(root):
        if root.endswith(('.yaml', '.yml')):
            yield root
        return
    for dirpath, dirnames, filenames in os.walk(root):
        dirnames[:] = [d for d in dirnames if d not in SKIP_DIRS]
        for name in sorted(filenames):
            if name.endswith(('.yaml', '.yml')):
                yield os.path.join(dirpath, name)


def check_yaml(path):
    """Return (declared, last, rows) for a YAML document, or None when it carries no history."""
    with open(path, encoding='utf-8') as fh:
        text = fh.read()
    declared = YAML_VERSION.search(text)
    hist = YAML_HIST.search(text)
    if not declared or not hist:
        return None
    rows = re.findall(r'^#\s*(v[\d.]+)\b', text[hist.end():], re.M)
    if not rows:
        return None
    return 'v' + declared.group(1), rows[-1], rows

def main(argv):
    roots = argv[1:] or ['.']
    checked = 0
    problems = []

    for root in roots:
        if not os.path.exists(root):
            print('check_doc_versions: no such path: %s' % root)
            return 2
        for path in find_yaml(root):
            display = display_path(path)
            if display not in REQUIRED_YAML:
                continue
            checked += 1
            result = check_yaml(path)
            if result is None:
                with open(path, encoding='utf-8') as fh:
                    text = fh.read()
                if not YAML_VERSION.search(text):
                    problems.append(
                        '%s\n    on the required list but has no "  version:" field under info\n'
                        '    fix: add one, so the contract declares the version it is' % display)
                else:
                    problems.append(
                        '%s\n    has an info.version but no "# Revision history" comment block\n'
                        '    fix: add one, with a "# vX.Y.Z" line per release' % display)
                continue
            declared, last, rows = result
            if declared != last:
                problems.append(
                    '%s\n    info.version says %s, the comment history reaches %s\n'
                    '    fix: set "version: %s" under info' % (display, declared, last,
                                                              last.lstrip('v')))
            duplicates = sorted({v for v in rows if rows.count(v) > 1})
            if duplicates:
                problems.append(
                    '%s\n    duplicated history label(s): %s\n'
                    '    fix: give the later entry the next number'
                    % (display, ', '.join(duplicates)))

        for path in find_markdown(root):
            display = display_path(path)
            required = display in REQUIRED
            result = check(path)
            if result is None:
                if not required:
                    continue
                with open(path, encoding='utf-8') as fh:
                    text = fh.read()
                if not HEADER.search(text):
                    problems.append(
                        '%s\n    on the required list but has no "| Version | vX |" row\n'
                        '    fix: add a metadata table with a Version row' % display)
                elif not REVISION_HEADING.search(text):
                    problems.append(
                        '%s\n    has a version header but no "## Revision history" section\n'
                        '    fix: add the section and one row per released version' % display)
                else:
                    problems.append(
                        '%s\n    has a revision history heading but no "| vX |" row under it\n'
                        '    fix: add a row for the current version' % display)
                checked += 1
                continue
            checked += 1
            header, last, rows = result

            if header != last:
                problems.append(
                    '%s\n    header says %s, the revision history reaches %s\n'
                    '    fix: set "| Version | %s |" in the header table'
                    % (display, header, last, last))

            duplicates = sorted({v for v in rows if rows.count(v) > 1})
            if duplicates:
                problems.append(
                    '%s\n    duplicated revision label(s): %s\n'
                    '    fix: give the later entry the next number'
                    % (display, ', '.join(duplicates)))

    if problems:
        print('check_doc_versions: %d versioned document(s) checked, %d problem(s)\n'
              % (checked, len(problems)))
        for p in problems:
            print('  ' + p)
            print()
        print('A document whose header disagrees with its own revision history is the')
        print('defect this check exists to catch, and a required document with no')
        print('header at all is the defect it used to skip. See change-log.md CR-012.')
        return 1

    print('check_doc_versions: %d versioned document(s) checked, all agree'
          % checked)
    return 0


if __name__ == '__main__':
    sys.exit(main(sys.argv))
