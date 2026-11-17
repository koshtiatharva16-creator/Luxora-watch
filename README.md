# LUXORA — Luxury Watch Store

A Spring Boot + Thymeleaf + MySQL starter storefront using a charcoal/black and champagne-gold design.

## Included
- Home page and collections catalog with 15 seeded watches
- Watch detail pages
- Session cart, quantity updates, remove item, checkout/order creation
- Login, registration, profile, wishlist, order history
- Brands, About, Contact pages
- Admin dashboard and add/edit/delete watch pages
- Reusable Thymeleaf fragments, responsive CSS and a small JavaScript file

## Requirements
- Java 17+
- Maven 3.9+
- MySQL 8+

## 1. Create the database
```sql
CREATE DATABASE luxora_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 2. Configure credentials
Edit `src/main/resources/application.properties`, or set environment variables:
- `DB_URL` (default `jdbc:mysql://localhost:3306/luxora_db?createDatabaseIfNotExist=true`)
- `DB_USERNAME` (default `root`)
- `DB_PASSWORD` (default empty)

## 3. Run
From this project folder:
```bash
mvn spring-boot:run
```
On Windows PowerShell, if Maven is installed use `mvn spring-boot:run`. This project does not include a Maven wrapper.

Open http://localhost:8080

## Deploy to Render

This repository includes a Render Blueprint (`render.yaml`) for deploying the
Dockerized Spring Boot app as a web service. The Blueprint does not create a
database: the app currently requires an externally hosted MySQL database.

1. Make sure your MySQL provider allows connections from Render and provides an
   SSL-capable connection.
2. In Render, create a new Blueprint from this repository and apply the
   `render.yaml` configuration.
3. Add the prompted environment values in Render:
   - `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD` for MySQL.
   - `LUXORA_ADMIN_EMAIL` and `LUXORA_ADMIN_PASSWORD` for the initial admin
     account. Choose a unique, strong password.
4. Deploy the service and open its `onrender.com` URL.

The web service is configured on Render's free plan, which may spin down after
inactivity. Use a paid instance if the app needs to stay available continuously.

## Demo admin
For a convenient local demo, first register a user, then promote that account in MySQL:
```sql
UPDATE app_users SET role='ADMIN' WHERE email='your-email@example.com';
```
Log out and back in. The admin dashboard is `/admin`.

## Important production notes
This is a functional learning project, not a production payment platform. Checkout creates an order record but does not charge a card/UPI. Add a payment gateway, email verification, rate limiting, HTTPS, secure session configuration, migrations, and stronger operational controls before production. Passwords are hashed with BCrypt. Admin routes are checked server-side; hiding links is not used as the security boundary.

## Watch images
The seed catalog uses remote Unsplash image URLs so the product cards have photographic watch images without adding large binary files. Internet access is needed to load them. Replace the image URLs with licensed/local product photography for a production store.

## Admin login

A default administrator account is created automatically on application startup if the email does not already exist:

- **Email:** `admin@luxora.com`
- **Password:** `LuxoraAdmin@2026`
- **Admin page:** `http://localhost:8080/admin`

The password is stored as a BCrypt hash. For a deployment, override the defaults with environment variables `LUXORA_ADMIN_EMAIL` and `LUXORA_ADMIN_PASSWORD` (Spring property environment-variable mapping), and use a unique secret. The initializer does not overwrite an existing account or password.

