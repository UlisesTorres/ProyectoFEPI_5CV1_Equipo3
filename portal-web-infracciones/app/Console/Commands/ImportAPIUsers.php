<?php

namespace App\Console\Commands;

use App\Models\Rol;
use App\Models\User;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Str;

class ImportAPIUsers extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'api:import-users';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Importa usuarios desde la base de datos de la API';

    /**
     * Execute the console command.
     */
    public function handle()
    {
        $this->info('Iniciando importación de usuarios...');

        $apiUsers = DB::connection('api_mysql')
            ->table('up_users')
            ->get();

        foreach ($apiUsers as $apiUser) {

            $pivot = DB::connection('api_mysql')
                ->table('up_users_role_lnk')
                ->where('user_id', $apiUser->id)
                ->first();

            $rolId = null;

            if ($pivot) {
                $rol = Rol::where('strapi_id', $pivot->role_id)->first();
                if ($rol) {
                    $rolId = $rol->id;
                }
            }

            $user = User::firstOrNew([
                'email' => $apiUser->email
            ]);

            $user->name = $apiUser->username ?? $apiUser->email;
            $user->api_id = $apiUser->id;
            $user->password = $apiUser->password;
            $user->rol_id = $rolId;
            $user->email_verified_at = now();

            $user->save();
        }

        $this->info('Usuarios importados correctamente con roles.');
    }
}
