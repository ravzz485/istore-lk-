# 🍎 iStore LK — Complete A-to-Z Build Roadmap (Beginner Friendly)

> **Project:** Apple Products E-Commerce & Retail Management System
> **Stack:** Angular 17 · Spring Boot 3 · MongoDB 7
> **Timeline:** ~12 weeks (part-time, beginner pace)
> **Goal:** A portfolio-grade project on GitHub + LinkedIn

---

## 📅 Timeline Overview

| Phase | Weeks | What You Build |
|-------|-------|----------------|
| 0. Prerequisites & Setup | Week 1–2 | Tools installed, basics learned |
| 1. Planning & Design | Week 3 | Diagrams, Figma wireframes, GitHub repo |
| 2. Backend (Spring Boot + MongoDB) | Week 4–6 | REST API with auth, products, orders |
| 3. Frontend (Angular) | Week 7–9 | Storefront + Admin panel |
| 4. Integration & Polish | Week 10 | Testing, seed data, invoices, emails |
| 5. Deployment | Week 11 | Live demo URL |
| 6. GitHub Polish + LinkedIn | Week 12 | README, demo video, LinkedIn posts |

**Golden rule:** Commit to GitHub from **Day 1**, not at the end. Recruiters look at your commit history — steady green squares over 12 weeks look far better than one giant upload.

---

## Phase 0 — Prerequisites & Setup (Week 1–2)

### 0.1 Install These Tools (in order)

| # | Tool | Why | Link |
|---|------|-----|------|
| 1 | Git | Version control | git-scm.com |
| 2 | Node.js LTS (v20) | Runs Angular tooling | nodejs.org |
| 3 | Angular CLI | `npm install -g @angular/cli@17` | — |
| 4 | JDK 21 (Temurin) | Runs Spring Boot | adoptium.net |
| 5 | IntelliJ IDEA Community | Best free Java IDE | jetbrains.com |
| 6 | VS Code | For Angular | code.visualstudio.com |
| 7 | MongoDB Community Server | Local database | mongodb.com/try/download/community |
| 8 | MongoDB Compass | GUI to view your data | comes with server installer |
| 9 | Postman | Test your API before UI exists | postman.com |

**Verify everything works:**
```bash
git --version
node -v          # should be v20.x
ng version       # Angular CLI 17.x
java -version    # should be 21
mongosh          # should connect to localhost:27017
```

### 0.2 Learn the Minimum Basics First (don't skip!)

You do NOT need to master everything. Learn just enough, then learn the rest *while building*.

| Topic | What to learn | Time | Free resource |
|-------|--------------|------|---------------|
| Java basics | Classes, interfaces, collections, streams | 3–4 days | "Java Full Course" — Bro Code (YouTube) |
| Spring Boot | What is a Controller/Service/Repository, REST | 2–3 days | "Spring Boot Tutorial" — Amigoscode (YouTube) |
| TypeScript | Types, interfaces, classes | 1–2 days | typescriptlang.org/docs handbook |
| Angular | Components, services, routing, HttpClient | 3–4 days | angular.dev official tutorial ("Tour of Heroes" style) |
| MongoDB | Documents, collections, CRUD, find queries | 1–2 days | MongoDB University free "Intro to MongoDB" |
| Git | clone, add, commit, push, branch, pull request | 1 day | "Git & GitHub for Beginners" — freeCodeCamp |

✅ **Checkpoint:** You can create a "Hello World" Spring Boot API and a "Hello World" Angular app, and push both to GitHub.

---

## Phase 1 — Planning & Design (Week 3)

### 1.1 Create the GitHub Repository (Day 1 of this phase)

1. Create a GitHub account with a **professional username** (e.g., `kasun-perera-dev`).
2. Create repo: `istore-lk` → Public → add README + `.gitignore` (choose "Java" for now).
3. Recommended structure (monorepo — easiest for beginners):
```
istore-lk/
├── backend/        # Spring Boot project
├── frontend/       # Angular project
├── docs/           # SRS PDF, diagrams, screenshots
└── README.md
```
4. Upload your **SRS PDF** into `docs/` — first commit! 🎉
5. Enable **GitHub Projects** (Kanban board): columns `Backlog → In Progress → Done`. Add cards for every phase below. Recruiters love seeing this.

**Commit message convention (use from day 1):**
```
feat: add product entity and repository
fix: cart total calculation for promo codes
docs: add architecture diagram
```

### 1.2 Draw Your Diagrams (use draw.io — free)

Create these and save as PNG into `docs/`:
1. **System Architecture Diagram** — Angular SPA → REST API → MongoDB, plus S3 & email service boxes.
2. **Use Case Diagram** — 3 actors (Customer, Staff, Admin) with their actions from the SRS.
3. **Collection/ER Diagram** — the 14 MongoDB collections and their references (from SRS Section 7).
4. **Order Status Flowchart** — Pending → Confirmed → Processing → Shipped/Ready → Delivered → Completed.

### 1.3 Design the UI in Figma (free plan)

1. Create a Figma account, create file "iStore LK".
2. Design these screens (desktop + mobile frame each):
   - Home (hero + category strip + product grid)
   - Product detail (image gallery + colour swatches + storage picker + price)
   - Cart & 3-step checkout
   - Login / Register
   - My Orders
   - Admin dashboard (KPI cards + charts)
   - Admin product form (with variant matrix)
3. **Style guide:** Francium/Apple-inspired — near-black `#171717`, white, accent blue `#0071e3`, font Inter or SF Pro-like, lots of whitespace, rounded corners (12px), subtle shadows.
4. Export screenshots into `docs/design/`.

✅ **Checkpoint:** Repo exists with SRS + 4 diagrams + Figma designs. You already have something to show!

---

## Phase 2 — Backend: Spring Boot + MongoDB (Week 4–6)

### 2.1 Create the Project (Week 4, Day 1)

1. Go to **start.spring.io**:
   - Project: Maven · Language: Java · Spring Boot: 3.x
   - Group: `com.istore` · Artifact: `api` · Java: 21
   - Dependencies: **Spring Web, Spring Data MongoDB, Spring Security, Validation, Lombok, Spring Boot DevTools**
2. Generate → unzip into `istore-lk/backend/`.
3. Add JWT library to `pom.xml`: `io.jsonwebtoken:jjwt-api`, `jjwt-impl`, `jjwt-jackson` (version 0.12.x).
4. `application.yml`:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/istore_db
server:
  port: 8080
```
5. Run the app → see "Started ApiApplication". Commit: `feat: initial spring boot setup`.

### 2.2 Build in This Exact Order (each step = 1 or more commits)

**Step 1 — User & Auth (Week 4)**
1. `User` document class: id, fullName, email, password, phone, nic, role (enum: CUSTOMER, STAFF, ADMIN), createdAt.
2. `UserRepository extends MongoRepository<User, String>` with `findByEmail`.
3. Register endpoint: validate → hash password with `BCryptPasswordEncoder(12)` → save.
4. Login endpoint: verify password → generate JWT (subject = userId, claim = role, expiry 15 min).
5. `JwtAuthFilter` — reads `Authorization: Bearer` header, sets Spring Security context.
6. `SecurityConfig` — permit `/api/v1/auth/**` and `GET /api/v1/products/**`; everything else authenticated; role rules with `hasRole`.
7. Test all with Postman. Save the Postman collection into `docs/`.

**Step 2 — Products & Variants (Week 5, first half)**
1. `Product` document with embedded `List<Variant>` (sku, color, storage, condition, price as `BigDecimal`/Decimal128, stock, images).
2. `specs` as a flexible `Map<String, Object>` — this is your MongoDB flexibility showcase.
3. Endpoints: `GET /products` (paginated + `?category=` filter), `GET /products/{slug}`, `POST /products` (ADMIN), `PATCH /products/{id}/variants/{sku}` (ADMIN).
4. Unique index on `variants.sku` and `slug` (`@Indexed(unique = true)` or via `MongoConfig`).
5. Seed 10–15 real Apple products (iPhone 16/17, MacBook Air M3, AirPods Pro...) using a `CommandLineRunner` seeder class. Use realistic LKR prices.

**Step 3 — Cart (Week 5, second half)**
1. `Cart` document: customerId, embedded items[] (sku, productId, qty, unitPrice).
2. Endpoints: `GET /cart`, `POST /cart/items`, `PATCH /cart/items/{sku}`, `DELETE /cart/items/{sku}`.

**Step 4 — Orders + the Star Feature: Transactional Stock (Week 6)**
This is your **interview highlight**. Do it carefully:
1. `Order` document: orderNo (`#IS-2026-00001`), customerId, items[] (sku + **snapshotted** name/price), delivery info, paymentMethod, status, statusHistory[], totals.
2. In `OrderService.placeOrder()` (annotate `@Transactional`):
   - For each item run an **atomic conditional update**:
     `updateOne({"variants.sku": sku, "variants.stock": {$gte: qty}}, {$inc: {"variants.$.stock": -qty}})`
   - If `modifiedCount == 0` → throw `OutOfStockException` → whole transaction rolls back.
   - Insert the order + a `StockMovement` (type SALE) in the same transaction.
3. ⚠️ MongoDB transactions need a **replica set**. Locally, run a single-node replica set:
   ```bash
   mongod --replSet rs0 --dbpath /your/db/path
   mongosh --eval "rs.initiate()"
   ```
   (Or use MongoDB Atlas free tier from the start — it's already a replica set.)
4. Status update endpoint (STAFF/ADMIN) that appends to statusHistory and restores stock on cancellation.

**Step 5 — Remaining Modules (end of Week 6)**
- Payments (record + slip URL + verify), Trade-ins, Reviews (verified purchase check), Notifications.
- **Dashboard aggregation:** monthly revenue via `MongoTemplate` aggregation pipeline (`$match` completed orders → `$group` by month → `$sum`). Another interview highlight.
- Add **SpringDoc OpenAPI** → Swagger UI at `/swagger-ui.html`. Screenshot it for the README.

✅ **Checkpoint:** Every SRS endpoint works in Postman. Swagger page looks professional. ~30+ commits on GitHub.

---

## Phase 3 — Frontend: Angular (Week 7–9)

### 3.1 Create the Project (Week 7, Day 1)

```bash
cd istore-lk
ng new frontend --routing --style=scss --standalone
cd frontend
ng serve   # open http://localhost:4200
```
Commit: `feat: initial angular setup`.

### 3.2 Foundation First (Week 7)

1. **Folder structure:**
```
src/app/
├── core/        # auth.service.ts, api.service.ts, jwt.interceptor.ts, auth.guard.ts
├── shared/      # navbar, footer, loader, badge, variant-picker components
├── features/
│   ├── auth/        # login, register pages
│   ├── store/       # home, catalogue, product-detail
│   ├── checkout/    # cart, checkout-stepper, confirmation
│   ├── account/     # my-orders, profile, trade-ins
│   └── admin/       # dashboard, products, orders, inventory (lazy-loaded)
└── models/      # product.model.ts, order.model.ts ...
```
2. `environment.ts` → `apiUrl: 'http://localhost:8080/api/v1'`.
3. **JWT interceptor** — attaches token to every request; on 401 → redirect to login.
4. **Auth guard + role guard** — protect `/account` (logged in) and `/admin` (ADMIN role from decoded JWT).
5. Enable **CORS** on the backend (`CorsConfig` allowing `http://localhost:4200`).
6. Build Login + Register pages with **Reactive Forms** + validation messages.

### 3.3 Storefront (Week 8)

1. **Navbar** — logo, category links, search box, cart icon with item-count badge, login/account menu.
2. **Home page** — hero banner, category strip, featured product grid.
3. **Product card** — image, name, "From LKR xxx,xxx", colour dots, availability badge.
4. **Product detail page (the showpiece):**
   - Image gallery that switches with colour selection
   - Colour swatch row + storage buttons (like apple.com / francium.lk)
   - Price updates via Angular **signals** when variant changes
   - "Add to Cart" disabled when stock = 0, shows "Only X left" when low
   - Specs table + reviews list
5. **Cart page** — line items, qty steppers, order summary card, promo code input.
6. **Checkout stepper** — Step 1 delivery/pickup → Step 2 payment method → Step 3 review & confirm → success page with order number.
7. Format all prices with Angular `currency` pipe → `'LKR '` prefix, comma separators.

### 3.4 Admin Panel (Week 9)

1. Separate layout with sidebar (Dashboard, Products, Orders, Inventory, Trade-ins, Customers).
2. **Dashboard** — 4 KPI cards + charts using **ng2-charts (Chart.js)**: monthly revenue bar chart, category sales donut. Data from your aggregation endpoints.
3. **Product management** — table with search; create/edit form including a **variant matrix editor** (add rows: colour, storage, price, stock).
4. **Order management** — table with status filter; detail drawer with status-progress buttons.
5. **Inventory** — stock list with low-stock highlighting; goods-received form.

✅ **Checkpoint:** Full flow works locally: register → browse → pick variant → cart → checkout → order appears in admin → status updated → customer sees it.

---

## Phase 4 — Integration & Polish (Week 10)

1. **Error handling** — global exception handler (backend, RFC 7807 style) + toast notifications (frontend).
2. **Loading states** — spinners/skeletons on every data fetch. Empty states ("Your cart is empty 🛒").
3. **Seed data** — polish your seeder: 15+ products, 3 users (customer/staff/admin), sample orders so the dashboard charts look alive in screenshots.
4. **PDF invoice** — backend generates with OpenPDF or iText; download button on order detail.
5. **Email (optional but impressive)** — SendGrid free tier (100 emails/day): order confirmation email with Thymeleaf template.
6. **Tests (even a few matter):**
   - Backend: JUnit tests for `OrderService` stock logic (the money shot: test that ordering more than stock fails).
   - Frontend: 2–3 component tests.
7. **Responsive pass** — test at 375px (mobile), 768px, 1280px.
8. **README first draft** with screenshots.

---

## Phase 5 — Deployment (Week 11) — All FREE tiers

| Piece | Service | Notes |
|-------|---------|-------|
| Database | **MongoDB Atlas** (M0 free) | Already a replica set → transactions work |
| Backend | **Render.com** or **Railway.app** | Deploy via Dockerfile or JAR; free tier sleeps when idle (fine for demo) |
| Frontend | **Vercel** or **Netlify** | `ng build` output; free custom subdomain |
| Images | **Cloudinary** (free) | Easier than S3 for beginners |

**Steps:**
1. Create Atlas cluster → get connection string → put in backend env var `SPRING_DATA_MONGODB_URI` (⚠️ NEVER commit passwords to GitHub — use environment variables).
2. Add a simple `Dockerfile` to backend:
```dockerfile
FROM eclipse-temurin:21-jre
COPY target/api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```
3. Deploy backend → get URL like `https://istore-api.onrender.com`.
4. Update Angular `environment.prod.ts` with that URL → deploy frontend to Vercel.
5. Update backend CORS to allow the Vercel domain.
6. Test the live site end-to-end. 🎉 **You now have a live demo URL.**

---

## Phase 6 — GitHub Polish + LinkedIn (Week 12)

### 6.1 The README (your project's landing page)

Structure it like this:
```markdown
# 🍎 iStore LK — Apple Products E-Commerce Platform
Full-stack e-commerce system inspired by Sri Lankan Apple resellers.

🔗 Live Demo: <link> · 📄 Full SRS: docs/SRS.pdf · 🎬 Demo Video: <link>

## ✨ Features
(bullet list with emojis — variants, transactional stock, dashboard, RBAC...)

## 🖼 Screenshots
(6–8 images: home, product detail, checkout, admin dashboard)

## 🏗 Architecture
(your architecture diagram image)

## 🛠 Tech Stack
Angular 17 · Spring Boot 3 · MongoDB 7 · JWT · Docker · Chart.js

## 🧠 Key Technical Decisions
- Embedded variants vs referenced orders (16MB doc limit)
- Atomic $inc + transactions to prevent overselling
- Price snapshotting for immutable order history
- Decimal128 for money

## 🚀 Run Locally
(exact commands for backend + frontend)

## 📚 Documentation
Links to SRS, diagrams, Postman collection
```

Also: add repo **About** description + topics (`angular`, `spring-boot`, `mongodb`, `ecommerce`, `java`, `typescript`), pin the repo on your profile, and create a **profile README** for your GitHub account.

### 6.2 Demo Video (2–3 minutes)

Record with OBS Studio (free) or Loom: browse → variant pick → checkout → admin dashboard → status update. Upload to YouTube (unlisted) and link in README + LinkedIn.

### 6.3 LinkedIn Strategy (don't just post once!)

**Profile updates first:**
- Headline: `Software Engineering Student | Full-Stack Developer | Angular · Spring Boot · MongoDB`
- Add project under **Featured** section with the demo video + GitHub link
- Skills: add Angular, Spring Boot, MongoDB, TypeScript, Java, REST APIs

**Post series (1 per week — much better reach than one post):**
1. **Kickoff post** (do this NOW): "I'm building a full-stack Apple products e-commerce platform from scratch — Angular + Spring Boot + MongoDB. Here's my SRS and designs. Follow along! 🧵" + Figma screenshots.
2. **Progress post:** "Week 5 update: solved concurrent-checkout overselling with MongoDB atomic updates + transactions. Here's how 👇" + code screenshot. (Technical posts like this attract recruiters.)
3. **Design post:** before/after UI screenshots.
4. **Launch post:** demo video + live link + GitHub + what you learned + tag `#Angular #SpringBoot #MongoDB #FullStack #SriLanka`.

**Writing tips:** first 2 lines must hook (they show before "see more"), tell the story of a problem you solved, always include an image/video, reply to every comment.

---

## ⚠️ Beginner Traps to Avoid

1. ❌ Trying to build everything at once → ✅ follow the phase order strictly; a working small thing beats a broken big thing.
2. ❌ Committing once a month → ✅ commit every working feature (aim 3+ commits/week).
3. ❌ Committing secrets (DB passwords, JWT secret) → ✅ environment variables + `.gitignore`.
4. ❌ Copying entire tutorials blindly → ✅ type code yourself; when stuck >30 min, search the exact error message.
5. ❌ Skipping the README → ✅ recruiters read READMEs, not code, first.
6. ❌ Perfectionism → ✅ ship V1, list improvements in a "Future Work" README section (payment gateway, Atlas Search, PWA).
7. ❌ Floating-point money → ✅ BigDecimal/Decimal128 always.

## 🎯 Definition of Done

- [ ] Live demo URL works on mobile & desktop
- [ ] GitHub: 60+ meaningful commits, README with screenshots, SRS + diagrams in /docs
- [ ] Demo video on YouTube
- [ ] 4+ LinkedIn posts published
- [ ] You can explain: JWT flow, embed-vs-reference, transaction/stock logic, aggregation pipeline — these WILL be interview questions

**ඔයාට පුළුවන්! 💪 Good luck!**
