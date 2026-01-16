<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;

class CheckRoleType
{
    public function handle(Request $request, Closure $next, ...$allowedTypes)
    {
        $user = auth()->user();

        if (!$user) {
            abort(404);
        }

        if (!$user->rol) {
            abort(404);
        }

        if (!in_array($user->rol->type, $allowedTypes, true)) {
            abort(404);
        }

        return $next($request);
    }
}
