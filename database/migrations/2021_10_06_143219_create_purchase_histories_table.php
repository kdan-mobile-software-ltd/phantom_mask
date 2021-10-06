<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreatePurchaseHistoriesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('purchase_histories', function (Blueprint $table) {
            $table->bigIncrements('id');
            $table->integer('user_id')->unsigned()->comment("消費者 (users.id)");
            $table->integer('mask_id')->unsigned()->comment("口罩 (masks.id)");
            $table->integer('purchase_number')->unsigned()->default(1)->comment("進貨數量 (組)");
            $table->decimal('transaction_amount', 8, 2)->default(0)->comment("實際交易金額");
            $table->dateTime('transaction_date');

            $table->foreign('user_id')
                ->references('id')->on('users')
                ->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('purchase_histories');
    }
}
