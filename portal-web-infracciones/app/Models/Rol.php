<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Rol extends Model
{
    protected $fillable = [
        'name',
        'type',
        'description',
        'strapi_id',
    ];

    public function users()
    {
        return $this->hasMany(User::class, 'rol_id');
    }
}
