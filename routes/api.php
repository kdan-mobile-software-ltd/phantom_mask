<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

// Route::middleware('auth:api')->get('/user', function (Request $request) {
//     return $request->user();
// });

Route::prefix('/pharmacy')->group(function () {
    Route::post('/list/period', "PharmacyController@getPharmaciesByPeriod");

    Route::get('/list', "PharmacyController@getPharmacies");
    Route::post('/list/masks', "PharmacyController@getMasksByPharmacy");

    Route::post('/list/price', "PharmacyController@getPharmaciesByPrice");
});

Route::prefix('/user')->group(function () {
    Route::post('/list/period', "UserController@getUsersByPeriod");

    Route::post('/list/transactions', "UserController@getTransactionsByPeriod");
});