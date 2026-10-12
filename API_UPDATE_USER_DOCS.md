# API Update User Documentation

## Endpoint
```
PUT /api/users/me
```

## Authentication
- **Required**: Yes (Bearer token)
- Lấy userId từ token hiện tại

## Request Body
```json
{
  "username": "string (optional)",
  "email": "string (optional)",
  "phoneNumber": "string (optional)",
  "avatarUrl": "string (optional)"
}
```

## Response (Success - 200 OK)
```json
{
  "type": "SUCCESS",
  "title": "Success",
  "status": 200,
  "code": "USER.UPDATE_SUCCESS",
  "message": "Cập nhật thông tin người dùng thành công",
  "data": {
    "id": "user_id",
    "username": "username",
    "email": "email@example.com",
    "phoneNumber": "0123456789",
    "status": "ACTIVE",
    "createdAt": "2026-05-27T10:00:00Z",
    "updatedAt": "2026-05-27T11:30:00Z",
    "emailVerified": true,
    "verifiedAt": "2026-05-27T10:05:00",
    "avatarUrl": "https://cloud.example.com/avatar.jpg",
    "providerType": "LOCAL",
    "roles": ["USER"]
  },
  "path": "/api/users/me",
  "traceId": null,
  "timestamp": "2026-05-27T11:30:00.123Z"
}
```

## Files Created/Modified

### New Files Created:
1. `UpdateUserRequest.java` - DTO request
2. `UpdateUserResponse.java` - DTO response  
3. `IUpdateUserUc.java` - Interface
4. `UpdateUserUcImpl.java` - Implementation

### Files Modified:
1. `ICoreUserService.java` - Added updateUser method
2. `CoreUserServiceImpl.java` - Added updateUser implementation
3. `UserController.java` - Added PUT endpoint

## Usage Example (Frontend)

```javascript
// JavaScript/Fetch
const updateUser = async (token, userData) => {
  const response = await fetch('/api/users/me', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      username: 'new_username',
      email: 'new_email@example.com',
      phoneNumber: '0987654321',
      avatarUrl: 'https://cloud.example.com/new_avatar.jpg'
    })
  });
  
  return response.json();
};
```

## Features
- ✅ Update user profile information
- ✅ Validate user existence (throw error if user not found)
- ✅ Auto-update `updatedAt` timestamp
- ✅ Return full updated user data
- ✅ Requires authentication
- ✅ Support partial updates (chỉ update field nào được gửi, các field khác giữ nguyên)

