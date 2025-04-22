# Phantom Mask 專案回應文件

## A. 專案概述與完成度

### A.1. 需求完成情況

- [x] 列出特定時間和星期幾營業的藥局
    - ✅ 已實作於 `POST /api/v1/pharmacy/queryOpeningHour`
    - 時間格式：HH:mm
    - 星期格式：`MON` ~ `SUN`

- [x] 列出指定藥局的口罩商品（可依名稱或價格排序）
    - ✅ 已實作於 `GET /api/v1/pharmacy/queryMasks/{id}`
    - 參數：`sortBy = NAME|PRICE`、`direction = ASC|DESC`

- [x] 列出特定價格範圍內且商品數量符合條件的藥局
    - ✅ 已實作於 `GET /api/v1/pharmacy/filter`
    - 參數：價格上下限、商品數量比較（compareType: `GREATER` 或 `LESS`）

- [x] 列出指定日期範圍內消費金額最高的用戶
    - ✅ 已實作於 `GET /api/v1/user/getTopUsers`
    - 篩選條件：`startDate`、`endDate`、`top`
    - 時間格式：yyyy-MM-dd

- [x] 統計指定日期範圍內的口罩銷售總量與金額
    - ✅ 已實作於 `GET /api/v1/mask/getMaskTransactionSummary`
    - 篩選條件：`startDate`、`endDate`
    - 時間格式：yyyy-MM-dd

- [x] 依相關度搜尋藥局或口罩
    - ✅ 已實作於 `POST /api/v1/search`
    - 參數：`keyword`、`type=PHARMACY|MASK`

- [x] 處理用戶購買口罩的交易流程
    - ✅ 已實作於 `POST /api/v1/mask/purchase`
    - 包含原子性交易處理
    - 自動處理庫存、用戶餘額、藥局餘額更新

---

### A.2. API 文件

- [API 文件說明](demo%2Fsrc%2Fmain%2Fjava%2FKADAN%2Finterview%2Fdemo%2Fdocs%2FApi.md)
- [Postman json](demo%2FKADAN_Phantom_Mask.json)

---

### A.3. JSON 資料載入

使用此 API 可一次性載入所有系統初始資料，包括藥局、口罩、使用者、庫存、營業時間與交易紀錄。

👉 `POST /api/v1/user/loadData`
> 每次執行都會清空現有資料，重新載入預設 JSON 測試檔案

---

## B. 技術實現

### B.1. 使用技術總覽

| 項目     | 技術                |
|--------|-------------------|
| 語言     | Java 17           |
| 框架     | Spring Boot 3     |
| ORM 工具 | JPA / Hibernate   |
| API 標準 | RESTful JSON API  |
| API 文件 | Swagger / OpenAPI |
| ERD 工具 | draw.io           |

---

## C. 設計架構

### C.1. ERD 實體關係圖

![ERD.png](demo%2FERD.png)
> 📌 ERD 說明：此系統共設計 6 張資料表，涵蓋藥局、口罩、使用者、庫存、交易紀錄與營業時間資訊，透過外鍵建立合理關聯。

#### 🔗 資料關聯說明

- 一間 `Pharmacy` 可有多筆 `OpeningTime`（一對多）
- 一間 `Pharmacy` 可販售多種 `Mask`，關聯於 `Pharmacy_Mask_Inventory`（多對多）
- 一筆 `Transaction` 會連結一位 `User` 與一間 `Pharmacy`（多對一）

---

### C.2. 專案架構說明

系統為典型的 Spring Boot 多層式架構，模組分層清楚，易於維護與擴展。

#### 📁 `entity`

- JPA 實體類別，對應資料庫表
- 自動透過 Hibernate 管理資料對應與關聯
- 包含：`Mask`, `Pharmacy`, `User`, `TransactionHistory`, `OpeningTime` 等

#### 📁 `repository`

- Spring Data JPA Repository 接口，負責資料存取
- 命名規則支援自動推導查詢
- 如需複雜查詢可自定義 `@Query`

#### 📁 `service` & `service.impl`

- `Service`：定義服務層接口（方便測試與依賴注入）
- `Impl`：實作具體商業邏輯，如交易處理、餘額計算、資料轉換等

#### 📁 `controller`

- 對外開放 API 的接口層
- 使用 `@RestController` 搭配 `@RequestMapping` 管理路徑與版本前綴 `/api/v1`
- 各控制器分工清楚：`MaskController`, `PharmacyController`, `UserController`, `SearchController`

#### 📁 `converter`

- `dto`：資料傳輸對象（Data Transfer Object），用於對外輸出，避免暴露內部結構
- `vo`：View Object，內部封裝前端所需結構
- `PurchaseRequest`：專用輸入模型，用於 POST 購買口罩的 API

#### 📁 `exception`

- `GlobalExceptionHandler`：統一處理錯誤訊息
- `ErrorResponse`：統一錯誤回應格式（message + errorCode）

#### 📁 `config`

- `OpenApiConfig`：Swagger/OpenAPI 設定，方便 API 文件自動產出與測試

#### 📁 `enumType`

- 所有查詢條件、排序參數、比對方式等定義為 Enum 類別：
    - `SortField`, `SortDirection`, `CompareType`, `SearchTarget`, `WeekDay`

---

### C.3. 技術整合文件（提供前端串接協助）

為 RESTful 架構設計，所有 API 均採 JSON 格式傳遞資料，並遵守 HTTP 標準狀態碼定義。

#### 🧾 基本規格

| 項目       | 說明                                                                         |
|----------|----------------------------------------------------------------------------|
| API 路徑前綴 | `/api/v1`                                                                  |
| 資料格式     | `application/json`                                                         |
| 錯誤格式     | 統一錯誤格式 `{ message, errorCode }`                                            |
| 狀態碼      | `200 OK`, `400 Bad Request`, `409 Conflict`, `500 Internal Server Error` 等 |

#### 🧾 文件與範例

- **Swagger 文件（本地）**：  
  `http://localhost:8080/swagger-ui/index.html`

- **範例請求/回應**：
  每一隻 API 的參數與範例皆已詳細定義在 Swagger 或 Markdown 文件中。
  👉 詳細說明請參考：[API 文件說明](demo%2Fsrc%2Fmain%2Fjava%2FKADAN%2Finterview%2Fdemo%2Fdocs%2FApi.md)
