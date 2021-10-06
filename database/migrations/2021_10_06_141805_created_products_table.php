<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreatedProductsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('products', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('pharmacy_id')->unsigned()->comment("所屬藥局 (pharmacies.id)");
            $table->integer('mask_id')->unsigned()->comment("口罩 (masks.id)");
            $table->integer('unit')->default(1)->unsigned()->comment("每組的單位");
            $table->decimal('price', 8, 2)->unsigned()->comment("每組的售價");
            $table->timestamps();

            $table->foreign('pharmacy_id')
                ->references('id')->on('pharmacies')
                ->onDelete('cascade');
            $table->foreign('mask_id')
                ->references('id')->on('masks')
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
        Schema::dropIfExists('products');
    }
}
