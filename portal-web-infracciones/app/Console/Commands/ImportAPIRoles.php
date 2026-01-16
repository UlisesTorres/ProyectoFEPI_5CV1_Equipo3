<?php

namespace App\Console\Commands;

use App\Models\Rol;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\DB;

class ImportAPIRoles extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'api:import-roles';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Importa roles desde Strapi';

    /**
     * Execute the console command.
     */
    public function handle()
    {
        $this->info('Importando roles desde Strapi...');

        $roles = DB::connection('api_mysql')
            ->table('up_roles')
            ->get();

        foreach ($roles as $role) {
            Rol::updateOrCreate(
                ['strapi_id' => $role->id],
                [
                    'name' => $role->name,
                    'type' => $role->type,
                    'description' => $role->description,
                ]
            );
        }

        $this->info('Roles importados correctamente.');
    }
}
