<?php

namespace App\Console\Commands;

use Illuminate\Console\Command;

class ImportAPIData extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'api:import-all';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Importa roles y usuarios desde la API en el orden correcto';

    /**
     * Execute the console command.
     */
    public function handle()
    {
        $this->info('Iniciando importación completa desde la API');
        $this->call('api:import-roles');
        $this->call('api:import-users');

        $this->info('Importación completa finalizada');
    }
}
