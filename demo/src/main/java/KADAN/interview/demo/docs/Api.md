<!-- TOC -->

* [⭐ Mask API](#-mask-api)
    * [1. Get Mask Transaction Summary](#1-get-mask-transaction-summary)
    * [2. Purchase Mask](#2-purchase-mask)
* [👤 User API](#-user-api)
    * [3. Load Initial Data](#3-load-initial-data)
    * [4. Get Top Users by Transaction Amount](#4-get-top-users-by-transaction-amount)
* [🏪 Pharmacy API](#-pharmacy-api)
    * [5. Get Pharmacies Open at a Specific Time](#5-get-pharmacies-open-at-a-specific-time)
    * [6. Get Masks Sold by a Pharmacy (with Sorting)](#6-get-masks-sold-by-a-pharmacy-with-sorting)
    * [7. Filter Pharmacies by Mask Price and Product Count](#7-filter-pharmacies-by-mask-price-and-product-count)
* [🔍 Search API](#-search-api)
    * [8. Full-text Search for Pharmacy or Mask](#8-full-text-search-for-pharmacy-or-mask)
* [⚠️ Common Error Response Format](#-common-error-response-format)

<!-- TOC -->

# ⭐ Mask API

**Base Path:** `/api/v1/mask`

---

### 1. Get Mask Transaction Summary

**Endpoint**  
`GET /mask/getMaskTransactionSummary`

**Description**  
Return total transaction amount and quantity for all masks within the specified date range.

**Query Parameters**

| Name      | Type   | Required | Description                       | Example      |
|-----------|--------|----------|-----------------------------------|--------------|
| startDate | `Date` | ✅        | Start date in `yyyy-MM-dd` format | `2021-01-01` |
| endDate   | `Date` | ✅        | End date in `yyyy-MM-dd` format   | `2021-01-31` |

**Response**

✅ `200 OK`

```json
{
  "totalTransactionAmount": 12345678.90,
  "totalQuantity": 2147483647
}
```

---

### 2. Purchase Mask

**Endpoint**  
`POST /mask/purchase`

**Description**  
User purchases a mask from a specific pharmacy.  
This operation updates user balance and records the transaction.

**Request Body**

| Field      | Type     | Required | Description                        | Example      |
|------------|----------|----------|------------------------------------|--------------|
| userId     | `Long`   | ✅        | ID of the user making the purchase | `1`          |
| pharmacyId | `Long`   | ✅        | ID of the pharmacy                 | `100`        |
| maskName   | `String` | ✅        | Name of the mask                   | `"N95 Mask"` |
| quantity   | `int`    | ✅        | Quantity to purchase               | `5`          |

**Sample Request**

```json
{
  "userId": 1,
  "pharmacyId": 100,
  "maskName": "N95 Mask",
  "quantity": 5
}
```

**Response**

✅ `200 OK`  
Returns transaction result:

```json
{
  "transactionId": 1001,
  "name": "user name",
  "pharmacyName": "pharmacy name",
  "maskName": "mask name",
  "quantity": 2147483647,
  "unitPrice": 12345678.90,
  "totalAmount": 12345678.90,
  "transactionDate": "yyyy-MM-dd HH:mm:ss",
  "userBalanceAfter": 12345678.90
}
```

---

# 👤 User API

**Base Path:** `/api/v1/user`

---

### 3. Load Initial Data

**Endpoint**  
`POST /user/loadData`

**Description**  
Clears all existing records and loads new data into the database from the bundled JSON files.

**Response**

✅ `200 OK`

```text
InitData Success!
```

---

### 4. Get Top Users by Transaction Amount

**Endpoint**  
`GET /user/getTopUsers`

**Description**  
Returns the top X users who have the highest total transaction amounts within the specified date range.

**Request Parameters**

| Name      | Type   | Required | Description                       | Example      |
|-----------|--------|----------|-----------------------------------|--------------|
| startDate | `Date` | ✅        | Start date in `yyyy-MM-dd` format | `2021-01-01` |
| endDate   | `Date` | ✅        | End date in `yyyy-MM-dd` format   | `2021-01-31` |
| top       | `int`  | ✅        | Number of top users to return     | `10`         |

**Response**

✅ `200 OK`

```json
[
  {
    "name": "user name",
    "totalAmount": 12345678.90
  },
  ...
]
```

---

# 🏪 Pharmacy API

**Base Path:** `/api/v1/pharmacy`

---

### 5. Get Pharmacies Open at a Specific Time

**Endpoint**  
`GET /pharmacy/queryOpeningHour`

**Description**  
Returns a list of pharmacies that are open at a specified time and day of the week.

**Request Parameters**

| Name    | Type     | Required | Description                                      | Example |
|---------|----------|----------|--------------------------------------------------|---------|
| weekDay | `Enum`   | ✅        | Day of the week. One of `MON`, `TUE`, ..., `SUN` | `MON`   |
| time    | `String` | ✅        | Time in `HH:mm` format                           | `08:00` |

**Enum: WeekDay**

| 值   | 說明（label） |
|------|----------------|
| MON  | Mon            |
| TUE  | Tue            |
| WED  | Wed            |
| THU  | Thu            |
| FRI  | Fri            |
| SAT  | Sat            |
| SUN  | Sun            |

**Response**

✅ `200 OK`

```json
[
  {
    "name": "pharmacy name",
    "cashBalance": 12345678.90,
    "masks": [
      {
        "name": "mask name",
        "price": 12345678.90,
        "packSize": 2147483647
      }
    ],
    "openingHours": [
      {
        "weekDay": "Mon",
        "startTime": "HH:mm:ss",
        "endTime": "HH:mm:ss"
      }
    ]
  }
]
```

---

### 6. Get Masks Sold by a Pharmacy (with Sorting)

**Endpoint**  
`GET /pharmacy/queryMasks/{id}`

**Description**  
Returns a list of masks sold by a pharmacy, optionally sorted by name or price.

**Path Parameter**

| Name | Type   | Required | Description | Example |
|------|--------|----------|-------------|---------|
| id   | `Long` | ✅        | Pharmacy ID | `101`   |

**Request Parameters**

| Name      | Type   | Required | Description                     | Example |
|-----------|--------|----------|---------------------------------|---------|
| sortBy    | `Enum` | ❌        | Sort by `NAME` or `PRICE`       | `NAME`  |
| direction | `Enum` | ❌        | Sort direction: `ASC` or `DESC` | `ASC`   |

**Enum: SortField**

| 值     | 說明      |
|--------|-----------|
| NAME   | 依口罩名稱排序 |
| PRICE  | 依口罩價格排序 |

**Enum: SortDirection**

| 值    | 說明         |
|-------|--------------|
| ASC   | 遞增（小 → 大） |
| DESC  | 遞減（大 → 小） |

**Response**

✅ `200 OK`

```json
[
  {
    "name": "mask name",
    "price": 12345678.90
  }
]
```

---

### 7. Filter Pharmacies by Mask Price and Product Count

**Endpoint**  
`GET /pharmacy/filter`

**Description**  
Returns a list of pharmacies that offer a certain number of mask products within the specified price range.

**Request Parameters**

| Name         | Type         | Required | Description                                | Example   |
|--------------|--------------|----------|--------------------------------------------|-----------|
| minPrice     | `BigDecimal` | ✅        | Minimum mask price                         | `10.99`   |
| maxPrice     | `BigDecimal` | ✅        | Maximum mask price                         | `15.01`   |
| productCount | `int`        | ✅        | Product count threshold (must be positive) | `3`       |
| compareType  | `Enum`       | ✅        | Comparison type: `GREATER` or `LESS`       | `GREATER` |

**Enum: CompareType**

| 值       | 說明              |
|----------|-------------------|
| GREATER  | 大於等於指定數量   |
| LESS     | 小於指定數量       |

**Response**

✅ `200 OK`

```json
[
  {
    "name": "pharmacy name",
    "cashBalance": 12345678.90,
    "masks": [
      {
        "name": "mask name",
        "price": 12345678.90,
        "packSize": 2147483647
      }
    ],
    "openingHours": [
      {
        "weekDay": "Mon",
        "startTime": "HH:mm:ss",
        "endTime": "HH:mm:ss"
      }
    ]
  }
]
```

---

# 🔍 Search API

**Base Path:** `/api/v1/search`

---

### 8. Full-text Search for Pharmacy or Mask

**Endpoint**  
`GET /search`

**Description**  
Searches for pharmacies or masks using a keyword.  
This uses MySQL full-text search and supports specifying the search type.

**Query Parameters**

| Name    | Type     | Required | Description                                                              | Example |
|---------|----------|----------|--------------------------------------------------------------------------|---------|
| keyword | `String` | ✅        | Keyword to search                                                        | `"N95"` |
| type    | `Enum`   | ❌        | Optional. Search target: `PHARMACY` or `MASK`. Default is searching both | `MASK`  |

**Enum: SearchTarget**

| 值       | 說明         |
|----------|--------------|
| PHARMACY | 搜尋藥局名稱 |
| MASK     | 搜尋口罩名稱 |

**Response**

✅ `200 OK`  
Returns a list of matched search results.

```json
[
  {
    "type": "PHARMACY | MASK",
    "name": "the name"
  },
  ...
]
```

---

# ⚠️ Common Error Response Format

❌ `400 Bad Request` – Invalid input

```json
{
  "message": "Invalid input",
  "errorCode": "BAD_REQUEST"
}
```

❌ `409 Conflict` – Insufficient balance or business conflict

```json
{
  "message": "Insufficient balance",
  "errorCode": "CONFLICT"
}
```

❌ `500 Internal Server Error`

```json
{
  "message": "Internal server error",
  "errorCode": "SERVER_ERROR"
}
```