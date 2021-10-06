<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class ModifyPharmacyAndProductOnPurchaseHistoriesTabel extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('purchase_histories', function (Blueprint $table) {
            $table->integer('pharmacy_id')->unsigned()->comment("藥局 (pharmacies.id)");
            $table->integer('product_id')->unsigned()->comment("各藥局販售的口罩 (products.id)");
            $table->dateTime('created_at');

            $table->foreign('product_id')
                ->references('id')->on('products')
                ->onDelete('cascade');
        });

        Schema::table('purchase_histories', function (Blueprint $table) {
            $table->dropColumn(['mask_id']);
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('purchase_histories', function (Blueprint $table) {
            $table->integer('mask_id')->unsigned()->comment("口罩 (masks.id)");
        });

        Schema::table('purchase_histories', function (Blueprint $table) {
            $table->dropColumn(['pharmacy_id', 'product_id', 'created_at']);
        });
    }
}
