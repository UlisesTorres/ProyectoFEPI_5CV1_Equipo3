<!-- Welcome.vue -->
<script setup>
import { Link } from '@inertiajs/vue3';
import PublicLayout from '@/Layouts/PublicLayout.vue';
import ApplicationMark from "@/Components/ApplicationMark.vue";

defineProps({
    canLogin: {
        type: Boolean,
    },
    canRegister: {
        type: Boolean,
    },
});
</script>

<template>
    <PublicLayout title="Portal de Infracciones CDMX">
        <div class="min-h-screen bg-gradient-to-br from-red-50 to-pink-100 dark:from-gray-900 dark:to-gray-800">
            <main>
                <section class="py-20 px-4 sm:px-6 lg:px-8">
                    <div class="max-w-7xl mx-auto text-center">
                        <div class="inline-flex items-center justify-center pb-10">
                            <ApplicationMark class="block h-28 w-auto" />
                        </div>
                        <h1 class="text-4xl sm:text-5xl lg:text-6xl font-bold text-gray-900 dark:text-white mb-6">
                            Portal de Infracciones
                            <span class="block text-red-600 dark:text-red-400 mt-2">Ciudad de México</span>
                        </h1>
                        <p class="text-xl text-gray-600 dark:text-gray-400 max-w-3xl mx-auto mb-10">
                            Consulta, gestiona y resuelve tus infracciones de tránsito de manera rápida y sencilla desde cualquier lugar
                        </p>
                        <div class="flex flex-col sm:flex-row gap-4 justify-center">
                            <Link
                                :href="route('infracciones.index')"
                                class="inline-flex items-center justify-center px-8 py-4 text-lg font-semibold text-white bg-red-600 hover:bg-red-700 dark:bg-red-500 dark:hover:bg-red-600 rounded-xl shadow-lg hover:shadow-xl transition duration-200"
                            >
                                <svg class="w-6 h-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                                </svg>
                                Consultar Infracciones
                            </Link>
                            <a
                                href="#servicios"
                                class="inline-flex items-center justify-center px-8 py-4 text-lg font-semibold text-gray-700 dark:text-gray-300 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700 rounded-xl shadow-lg hover:shadow-xl transition duration-200 border border-gray-200 dark:border-gray-700"
                            >
                                Conocer Servicios
                            </a>
                        </div>
                    </div>
                </section>

                <section id="servicios" class="py-16 px-4 sm:px-6 lg:px-8 bg-white dark:bg-gray-800">
                    <div class="max-w-7xl mx-auto">
                        <div class="text-center mb-16">
                            <h2 class="text-3xl sm:text-4xl font-bold text-gray-900 dark:text-white mb-4">
                                Servicios Disponibles
                            </h2>
                            <p class="text-lg text-gray-600 dark:text-gray-400 max-w-2xl mx-auto">
                                Todo lo que necesitas para gestionar tus infracciones de tránsito en un solo lugar
                            </p>
                        </div>

                        <div class="grid md:grid-cols-3 gap-8">
                            <!-- Card 1: Consulta de Infracciones -->
                            <Link
                                :href="route('infracciones.index')"
                                class="block bg-gradient-to-br from-red-50 to-pink-50 dark:from-gray-700 dark:to-gray-800 rounded-2xl p-8 border border-red-100 dark:border-gray-600 hover:shadow-xl hover:scale-105 transition-all duration-300 cursor-pointer group"
                            >
                                <div class="w-14 h-14 bg-red-600 dark:bg-red-500 rounded-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform duration-300">
                                    <svg class="w-7 h-7 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
                                    </svg>
                                </div>
                                <h3 class="text-xl font-bold text-gray-900 dark:text-white mb-3 group-hover:text-red-600 dark:group-hover:text-red-400 transition-colors">
                                    Consulta de Infracciones
                                </h3>
                                <p class="text-gray-600 dark:text-gray-400 mb-4">
                                    Busca y consulta todas las infracciones asociadas a tu vehículo ingresando únicamente el número de placa. Obtén información detallada de cada infracción.
                                </p>
                                <ul class="space-y-2 text-sm text-gray-600 dark:text-gray-400">
                                    <li class="flex items-center gap-2">
                                        <svg class="w-4 h-4 text-red-600 dark:text-red-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                        </svg>
                                        Búsqueda por placa vehicular
                                    </li>
                                    <li class="flex items-center gap-2">
                                        <svg class="w-4 h-4 text-red-600 dark:text-red-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                        </svg>
                                        Detalles completos de infracciones
                                    </li>
                                    <li class="flex items-center gap-2">
                                        <svg class="w-4 h-4 text-red-600 dark:text-red-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                        </svg>
                                        Evidencia fotográfica
                                    </li>
                                </ul>
                                <div class="mt-6 flex items-center text-red-600 dark:text-red-400 font-semibold text-sm group-hover:translate-x-2 transition-transform duration-300">
                                    Ir a consultar
                                    <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                                    </svg>
                                </div>
                            </Link>

                            <!-- Card 2: Pagos Pendientes -->
                            <Link
                                :href="route('pagos.index')"
                                class="block bg-gradient-to-br from-amber-50 to-orange-50 dark:from-gray-700 dark:to-gray-800 rounded-2xl p-8 border border-amber-100 dark:border-gray-600 hover:shadow-xl hover:scale-105 transition-all duration-300 cursor-pointer group"
                            >
                                <div class="w-14 h-14 bg-amber-600 dark:bg-amber-500 rounded-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform duration-300">
                                    <svg class="w-7 h-7 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"></path>
                                    </svg>
                                </div>
                                <h3 class="text-xl font-bold text-gray-900 dark:text-white mb-3 group-hover:text-amber-600 dark:group-hover:text-amber-400 transition-colors">
                                    Pagos Pendientes
                                </h3>
                                <p class="text-gray-600 dark:text-gray-400 mb-4">
                                    Visualiza el monto total de tus adeudos, fechas límite de pago y opciones disponibles para liquidar tus infracciones de manera segura.
                                </p>
                                <ul class="space-y-2 text-sm text-gray-600 dark:text-gray-400">
                                    <li class="flex items-center gap-2">
                                        <svg class="w-4 h-4 text-amber-600 dark:text-amber-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                        </svg>
                                        Estado de pagos en tiempo real
                                    </li>
                                    <li class="flex items-center gap-2">
                                        <svg class="w-4 h-4 text-amber-600 dark:text-amber-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                        </svg>
                                        Montos y fechas límite
                                    </li>
                                    <li class="flex items-center gap-2">
                                        <svg class="w-4 h-4 text-amber-600 dark:text-amber-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                        </svg>
                                        Historial de transacciones
                                    </li>
                                </ul>
                                <div class="mt-6 flex items-center text-amber-600 dark:text-amber-400 font-semibold text-sm group-hover:translate-x-2 transition-transform duration-300">
                                    Ver pagos
                                    <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                                    </svg>
                                </div>
                            </Link>

                            <!-- Card 3: Generación de Formatos -->
                            <Link
                                :href="route('infracciones.crear')"
                                class="block bg-gradient-to-br from-blue-50 to-indigo-50 dark:from-gray-700 dark:to-gray-800 rounded-2xl p-8 border border-blue-100 dark:border-gray-600 hover:shadow-xl hover:scale-105 transition-all duration-300 cursor-pointer group"
                            >
                                <div class="w-14 h-14 bg-blue-600 dark:bg-blue-500 rounded-xl flex items-center justify-center mb-6 group-hover:scale-110 transition-transform duration-300">
                                    <svg class="w-7 h-7 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                                    </svg>
                                </div>
                                <h3 class="text-xl font-bold text-gray-900 dark:text-white mb-3 group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">
                                    Generación de Formatos
                                </h3>
                                <p class="text-gray-600 dark:text-gray-400 mb-4">
                                    Genera automáticamente los formatos oficiales necesarios para apelar infracciones. Descarga documentos listos para presentar ante las autoridades.
                                </p>
                                <ul class="space-y-2 text-sm text-gray-600 dark:text-gray-400">
                                    <li class="flex items-center gap-2">
                                        <svg class="w-4 h-4 text-blue-600 dark:text-blue-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                        </svg>
                                        Formatos de apelación oficiales
                                    </li>
                                    <li class="flex items-center gap-2">
                                        <svg class="w-4 h-4 text-blue-600 dark:text-blue-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                        </svg>
                                        Descarga en PDF
                                    </li>
                                    <li class="flex items-center gap-2">
                                        <svg class="w-4 h-4 text-blue-600 dark:text-blue-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                        </svg>
                                        Guía paso a paso
                                    </li>
                                </ul>
                                <div class="mt-6 flex items-center text-blue-600 dark:text-blue-400 font-semibold text-sm group-hover:translate-x-2 transition-transform duration-300">
                                    Generar formato
                                    <svg class="w-4 h-4 ml-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                                    </svg>
                                </div>
                            </Link>
                        </div>
                    </div>
                </section>

                <section class="py-16 px-4 sm:px-6 lg:px-8 bg-gradient-to-br from-red-50 to-pink-100 dark:from-gray-900 dark:to-gray-800">
                    <div class="max-w-7xl mx-auto">
                        <div class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-8 md:p-12 border border-gray-200 dark:border-gray-700">
                            <div class="grid md:grid-cols-2 gap-8 items-center">
                                <div>
                                    <h2 class="text-3xl font-bold text-gray-900 dark:text-white mb-4">
                                        ¿Cómo funciona?
                                    </h2>
                                    <p class="text-gray-600 dark:text-gray-400 mb-6">
                                        Consultar tus infracciones es muy sencillo. Solo sigue estos pasos:
                                    </p>
                                    <div class="space-y-4">
                                        <div class="flex items-start gap-4">
                                            <div class="w-8 h-8 bg-red-600 dark:bg-red-500 text-white rounded-full flex items-center justify-center font-bold flex-shrink-0">
                                                1
                                            </div>
                                            <div>
                                                <h4 class="font-semibold text-gray-900 dark:text-white mb-1">Ingresa tu placa</h4>
                                                <p class="text-sm text-gray-600 dark:text-gray-400">Escribe el número de placa de tu vehículo en el formulario de búsqueda</p>
                                            </div>
                                        </div>
                                        <div class="flex items-start gap-4">
                                            <div class="w-8 h-8 bg-red-600 dark:bg-red-500 text-white rounded-full flex items-center justify-center font-bold flex-shrink-0">
                                                2
                                            </div>
                                            <div>
                                                <h4 class="font-semibold text-gray-900 dark:text-white mb-1">Consulta resultados</h4>
                                                <p class="text-sm text-gray-600 dark:text-gray-400">Visualiza todas las infracciones asociadas con detalles completos</p>
                                            </div>
                                        </div>
                                        <div class="flex items-start gap-4">
                                            <div class="w-8 h-8 bg-red-600 dark:bg-red-500 text-white rounded-full flex items-center justify-center font-bold flex-shrink-0">
                                                3
                                            </div>
                                            <div>
                                                <h4 class="font-semibold text-gray-900 dark:text-white mb-1">Toma acción</h4>
                                                <p class="text-sm text-gray-600 dark:text-gray-400">Paga tus multas o genera formatos para apelar según sea necesario</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="bg-gradient-to-br from-red-100 to-pink-100 dark:from-gray-700 dark:to-gray-600 rounded-xl p-8 flex items-center justify-center">
                                    <svg class="w-64 h-64 text-red-600 dark:text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                                    </svg>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </main>

            <footer class="bg-white dark:bg-gray-800 border-t border-gray-200 dark:border-gray-700 py-8">
                <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div class="text-center text-gray-600 dark:text-gray-400">
                        <p>&copy; 2024 Portal de Infracciones de Tránsito. Todos los derechos reservados.</p>
                    </div>
                </div>
            </footer>
        </div>
    </PublicLayout>
</template>
