# bitespeed

## Hosted URL

The application is hosted on Render:

🔗 **Base URL:** [https://bitespeed-39va.onrender.com](https://bitespeed-39va.onrender.com)

---

## API Endpoint

### POST `/identify`

**URL:**  
`https://bitespeed-39va.onrender.com/identify`

**Request Body:**

```json
{
  "email": "mcfly@hillvalley.edu",
  "phoneNumber": "123456"
}
```

**Response Body:**

```json
{
  "contact": {
    "primaryContatctId": 1,
    "emails": ["lorraine@hillvalley.edu", "mcfly@hillvalley.edu"],
    "phoneNumbers": ["123456"],
    "secondaryContactIds": [23]
  }
}
```
Note: Both email and phoneNumber are optional, but at least one must be present.
