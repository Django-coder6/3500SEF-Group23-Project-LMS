# Stage 1: Build stage (Use Node 24 to build static files)
FROM node:24 AS builder
WORKDIR /app

# Consumed by Vite at build time (see .env.example)
ARG VITE_API_BASE_URL=/api
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm ci

# Copy the rest of the source code and execute the build (Vite)
COPY . .
RUN npm run build

# Stage 2: Serve stage (Use Nginx to provide static web services)
FROM nginx:alpine

# Copy the static files compiled by the frontend to the default hosting directory of Nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# Declare the default Nginx port
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]