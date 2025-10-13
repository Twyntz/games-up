```mermaid
graph TD
    UI[Angular Frontend]
    JavaAPI[Java Spring Backend]
    PythonML[Python ML Backend]
    DB[(MySQL DB)]

    UI -->|REST over HTTPS| JavaAPI
    JavaAPI -->|JDBC| DB
    JavaAPI -->|REST over HTTPS| PythonML
```