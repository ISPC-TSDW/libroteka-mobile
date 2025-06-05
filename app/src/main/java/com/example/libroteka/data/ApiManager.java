package com.example.libroteka.data;

import android.content.Context;
import android.util.Log;

import com.example.libroteka.SessionManager;
import com.example.libroteka.retrofit.ApiInterface;
import com.example.libroteka.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager {
    private ApiInterface apiInterface;
    private SessionManager sessionManager;
    private MyApp app;

    public ApiManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.apiInterface = RetrofitClient.getRetrofitInstance(sessionManager).create(ApiInterface.class);
        // Initialize app using the context from SessionManager
        this.app = (MyApp) sessionManager.getContext().getApplicationContext();
    }

    // Método para refrescar el token
//    private void refreshToken(final ApiCallback<Void> callback) {
//        Call<TokenResponse> call = apiInterface.refreshToken(app.getRefreshToken());
//
//        call.enqueue(new Callback<TokenResponse>() {
//            @Override
//            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    app.setAccessToken(response.body().getAccess());
//                    app.setRefreshToken(response.body().getRefreshToken());
//                    callback.onSuccess(null);
//                } else {
//                    callback.onFailure("Token refresh failed");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TokenResponse> call, Throwable t) {
//                callback.onFailure("Token refresh failed: " + t.getMessage());
//            }
//        });
//    }


    public void loginUser(LoginRequest loginRequest, final ApiCallback<UserResponse> callback) {
        Call<UserResponse> call = apiInterface.loginUser(loginRequest);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    app.setUserEmail(String.valueOf(loginRequest.getEmail()));
                    app.setUserId(response.body().getUserId());
                } else {
                    callback.onFailure("Login fallido: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                callback.onFailure("Login fallido: " + t.getMessage());
            }
        });
    }

    public void getToken(TokenRequest tokenRequest, final ApiCallback<TokenResponse> callback) {
        Call<TokenResponse> call = apiInterface.getToken(tokenRequest);
// Call the getToken API
        call.enqueue(new Callback<TokenResponse>() {

            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Token fallido: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                callback.onFailure("Token fallido: " + t.getMessage());
            }
        });
    }

    public void getBooks(final ApiCallback<List<BookResponse>> callback) {
        Call<List<BookResponse>> call = apiInterface.getBooks();

        call.enqueue(new Callback<List<BookResponse>>() {
            @Override
            public void onResponse(Call<List<BookResponse>> call, Response<List<BookResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Hubo un error al intentar recuperar los libros: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<BookResponse>> call, Throwable t) {
                callback.onFailure("Hubo un error al intentar recuperar los libros: " + t.getMessage());
            }
        });
    }

    public void getAuthors(ApiCallback<List<Author>> callback) {
        apiInterface.getAuthors().enqueue(new Callback<List<Author>>() {
            @Override
            public void onResponse(Call<List<Author>> call, Response<List<Author>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("No se pudieron obtener los autores.");
                }
            }

            @Override
            public void onFailure(Call<List<Author>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }


    public void registerUser(RegisterRequest registerRequest, final ApiCallback<RegisterResponse> callback) {
        // Make the call to the register API
        Call<RegisterResponse> call = apiInterface.registerUser(registerRequest);

        // Handle the API response asynchronously
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Registration successful, return the success callback
                    callback.onSuccess(response.body());
                } else {
                    // Registration failed, return the failure callback
                    callback.onFailure("Registration failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                // Handle the error during the call (e.g., network issue)
                callback.onFailure("Registration failed: " + t.getMessage());
            }
        });
    }

    public void getFavorites(String userId, final ApiCallback<List<FavoriteRequest>> callback) {
        Call<List<FavoriteRequest>> call = apiInterface.getFavorites(userId);

        call.enqueue(new Callback<List<FavoriteRequest>>() {
            @Override
            public void onResponse(Call<List<FavoriteRequest>> call, Response<List<FavoriteRequest>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Hubo un error al intentar recuperar los libros: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FavoriteRequest>> call, Throwable t) {
                callback.onFailure("Hubo un error al intentar recuperar tus favoritos: " + t.getMessage());
            }
        });
    }

    public void getFavoriteStatus(String userId, int bookId, final ApiCallback<Boolean> callback) {
        // Create a request model or object to send, if necessary
        // FavoriteRequest request = new FavoriteRequest(userId, bookId); // Remove this if not needed

        // Adjust the API call to match the return type of Boolean
        Call<Boolean> call = apiInterface.getFavoriteStatus(userId, bookId); // Pass userId and bookId

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to retrieve favorite status: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onFailure("Failed to retrieve favorite status: " + t.getMessage());
            }
        });
    }

    // New method for toggling favorite books
    public void toggleFavorite(String userId, Integer bookId, final ApiCallback<Void> callback) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(userId, bookId);
        Call<Void> call = apiInterface.toggleFavorite(favoriteRequest);
        Log.i("AVER", favoriteRequest.toString());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null); // No response body needed for a successful toggle
                } else {
                    callback.onFailure("Toggle favorite failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Toggle favorite failed: " + t.getMessage());
            }
        });
    }

    public void removeFavorite(Integer favId, final ApiCallback<Void> callback) {
        Call<Void> call = apiInterface.removeFavorite(favId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null); // No response body needed for a successful toggle
                } else {
                    callback.onFailure("Toggle favorite failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("Toggle favorite failed: " + t.getMessage());
            }
        });
    }
    public void updateUserProfile(UpdateProfileRequest updateProfileRequest, final ApiCallback<UpdateResponse> callback) {
        Call<UpdateResponse> call = apiInterface.updateUserProfile(updateProfileRequest);

        call.enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Profile update failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
                callback.onFailure("Profile update failed: " + t.getMessage());
            }
        });
    }
    public void deleteUserProfile(DeleteProfileRequest deleteProfileRequest, final ApiCallback<DeleteResponse> callback) {
        Call<DeleteResponse> call = apiInterface.deleteUserProfile(deleteProfileRequest);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Profile update failed: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                callback.onFailure("Profile update failed: " + t.getMessage());
            }
        });

    }

    public void getUser(String email, final ApiCallback<GetUserResponse> callback) {
        Call<GetUserResponse> call = apiInterface.getUser(email);

        call.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("User fetch failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                callback.onFailure("User fetch failed: " + t.getMessage());
            }
        });
    }
    // Método para obtener el usuario por email
    public void getUserByEmail(String email, final ApiCallback<GetUserResponse> callback) {
        Call<GetUserResponse> call = apiInterface.getUserByEmail(email);
        Log.i("EMAIL en getuserbyemail", email);
        call.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("User fetch failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                callback.onFailure("User fetch failed: " + t.getMessage());
            }
        });
    }


    public interface ApiCallback<T> {
        void onSuccess(T response);
        void onFailure(String errorMessage);
    }

}