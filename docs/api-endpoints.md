# 📘 GamesUP API – Endpoints REST

| 🧩 Entité | 🔐 Endpoints Admin                                                                     | 👤 Endpoints Client                         |
|-----------|----------------------------------------------------------------------------------------|---------------------------------------------|
| Auth      | –                                                                                      | `POST /auth/register`<br>`POST /auth/login` |
| Author    | `POST /admin/authors`<br>`PUT /admin/authors/:id`<br>`DEL /admin/authors/:id`          | `GET /authors`<br>`GET /authors/:id`        |
| Category  | `POST /admin/categories`<br>`PUT /admin/categories/:id`<br>`DEL /admin/categories/:id` | `GET /categories`<br>`GET /categories/:id`        |
| Publisher | `POST /admin/publishers`<br>`PUT /admin/publishers/:id`<br>`DEL /admin/publishers/:id` | `GET /publishers`<br>`GET /publishers/:id`        |