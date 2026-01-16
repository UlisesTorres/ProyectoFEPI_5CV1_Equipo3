<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Inertia\Inertia;

class InfocdmxController extends Controller
{
    public function index()
    {
        return Inertia::render('InfoCDMX/Dashboard');
    }
}
