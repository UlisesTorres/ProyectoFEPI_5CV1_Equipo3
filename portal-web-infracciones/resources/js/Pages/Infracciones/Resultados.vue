<script setup>
import { Link } from '@inertiajs/vue3';
import { ref } from 'vue';
import PublicLayout from "@/Layouts/PublicLayout.vue";

const props = defineProps({
    placa_consultada: String,
    lista_infracciones: Array,
    strapi_url: String,
});

const imagenSeleccionada = ref(null);
const folioCopiadoId = ref(null);
const mostrarNotificacion = ref(false);
const folioCopiado = ref('');

// --- NUEVO: Estado para controlar qué imágenes ya cargaron ---
const imagenesCargadas = ref({});

const marcarImagenComoCargada = (identificador) => {
    imagenesCargadas.value[identificador] = true;
};
// -------------------------------------------------------------

const abrirZoom = (url) => {
    if (url) {
        imagenSeleccionada.value = url;
        document.body.style.overflow = 'hidden';
    }
};

const cerrarZoom = () => {
    imagenSeleccionada.value = null;
    document.body.style.overflow = 'auto';
};

const copiarFolio = async (folio, id) => {
    try {
        await navigator.clipboard.writeText(folio);
        folioCopiadoId.value = id;
        folioCopiado.value = folio;
        mostrarNotificacion.value = true;

        setTimeout(() => {
            folioCopiadoId.value = null;
        }, 2000);

        setTimeout(() => {
            mostrarNotificacion.value = false;
        }, 5000);
    } catch (err) {
        console.error('Error al copiar:', err);
    }
};

const formatearFecha = (fechaIso) => {
    if (!fechaIso) return 'Sin fecha';
    return new Date(fechaIso).toLocaleDateString('es-MX', {
        year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
    });
};

const getImagenUrl = (urlParcial) => {
    if (!urlParcial) return null;
    if (urlParcial.startsWith('http')) return urlParcial;
    return `${props.strapi_url}${urlParcial}`;
};
</script>

<template>
    <PublicLayout title="Resultados">
        <div class="min-h-screen bg-gradient-to-br from-red-50 to-pink-100 dark:from-gray-900 dark:to-gray-800 py-8 px-4 sm:px-6 lg:px-8">
            <div class="max-w-6xl mx-auto">
                <!-- Encabezado (Sin cambios) -->
                <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-8 gap-4">
                    <div>
                        <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-2">
                            Resultados de Consulta
                        </h1>
                        <div class="flex items-center gap-2">
                            <span class="text-gray-600 dark:text-gray-400">Placa:</span>
                            <span class="inline-flex items-center px-3 py-1 rounded-full text-sm font-semibold bg-red-100 dark:bg-red-900 text-red-800 dark:text-red-200">
                                {{ placa_consultada }}
                            </span>
                        </div>
                    </div>
                    <Link
                        :href="route('infracciones.index')"
                        class="inline-flex items-center px-4 py-2 bg-white dark:bg-gray-800 border border-gray-300 dark:border-gray-600 rounded-lg text-sm font-medium text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 dark:focus:ring-offset-gray-900 transition duration-200 shadow-sm"
                    >
                        <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                        </svg>
                        Nueva Consulta
                    </Link>
                </div>

                <!-- Estado Sin Infracciones (Sin cambios) -->
                <div v-if="!lista_infracciones || lista_infracciones.length === 0" class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl p-12 text-center border border-gray-200 dark:border-gray-700">
                    <div class="inline-flex items-center justify-center w-20 h-20 bg-green-100 dark:bg-green-900 rounded-full mb-6">
                        <svg class="w-10 h-10 text-green-600 dark:text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                    </div>
                    <h3 class="text-2xl font-bold text-gray-900 dark:text-white mb-2">
                        Sin Infracciones Registradas
                    </h3>
                    <p class="text-gray-600 dark:text-gray-400 max-w-md mx-auto">
                        No se encontraron infracciones para la placa consultada. El vehículo no tiene adeudos pendientes.
                    </p>
                </div>

                <!-- Lista de Infracciones -->
                <div v-else class="space-y-6">
                    <div class="bg-white dark:bg-gray-800 rounded-lg shadow-sm p-4 border border-gray-200 dark:border-gray-700">
                        <div class="flex items-center justify-between">
                            <span class="text-sm font-medium text-gray-700 dark:text-gray-300">
                                Total de infracciones encontradas:
                            </span>
                            <span class="inline-flex items-center px-3 py-1 rounded-full text-sm font-bold bg-red-100 dark:bg-red-900 text-red-800 dark:text-red-200">
                                {{ lista_infracciones.length }}
                            </span>
                        </div>
                    </div>

                    <div
                        v-for="item in lista_infracciones"
                        :key="item.id"
                        class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl overflow-hidden border border-gray-200 dark:border-gray-700 hover:shadow-2xl transition-shadow duration-300"
                    >
                        <div class="flex flex-col lg:flex-row">
                            <!-- Columna Izquierda: Datos (Sin cambios mayores) -->
                            <div class="flex-1 p-6 space-y-4">
                                <div class="flex flex-col sm:flex-row sm:justify-between sm:items-start gap-4">
                                    <div class="flex-1">
                                        <div class="inline-flex items-center gap-2">
                                            <div class="inline-flex items-center px-3 py-1 rounded-lg bg-gray-100 dark:bg-gray-700 text-gray-800 dark:text-gray-200 text-xs font-bold">
                                                <svg class="w-3 h-3 mr-1" fill="currentColor" viewBox="0 0 20 20">
                                                    <path d="M9 2a1 1 0 000 2h2a1 1 0 100-2H9z"></path>
                                                    <path fill-rule="evenodd" d="M4 5a2 2 0 012-2 3 3 0 003 3h2a3 3 0 003-3 2 2 0 012 2v11a2 2 0 01-2 2H6a2 2 0 01-2-2V5zm3 4a1 1 0 000 2h.01a1 1 0 100-2H7zm3 0a1 1 0 000 2h3a1 1 0 100-2h-3zm-3 4a1 1 0 100 2h.01a1 1 0 100-2H7zm3 0a1 1 0 100 2h3a1 1 0 100-2h-3z" clip-rule="evenodd"></path>
                                                </svg>
                                                FOLIO: {{ item.folio || 'S/N' }}
                                            </div>
                                            <button
                                                v-if="item.folio"
                                                @click="copiarFolio(item.folio, item.id)"
                                                class="inline-flex items-center justify-center p-1.5 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 text-gray-600 dark:text-gray-400 hover:text-gray-900 dark:hover:text-gray-200 transition-all duration-200 group relative"
                                                :class="{ 'bg-green-100 dark:bg-green-900': folioCopiadoId === item.id }"
                                                :title="folioCopiadoId === item.id ? '¡Copiado!' : 'Copiar folio'"
                                            >
                                                <svg v-if="folioCopiadoId === item.id" class="w-4 h-4 text-green-600 dark:text-green-400" fill="currentColor" viewBox="0 0 20 20">
                                                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                                                </svg>
                                                <svg v-else class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
                                                </svg>
                                            </button>
                                        </div>
                                        <h3 class="text-2xl font-bold text-gray-900 dark:text-white mb-2 mt-3">
                                            {{ item.tipo_infraccion_id?.nombre || 'Infracción General' }}
                                        </h3>
                                    </div>
                                    <div class="text-left sm:text-right">
                                        <p class="text-xs text-gray-500 dark:text-gray-400 uppercase font-semibold mb-1">Fecha</p>
                                        <p class="text-sm font-bold text-gray-900 dark:text-white">
                                            {{ formatearFecha(item.fecha_infraccion) }}
                                        </p>
                                    </div>
                                </div>

                                <div class="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-4">
                                    <div class="flex items-start gap-2">
                                        <svg class="w-5 h-5 text-gray-500 dark:text-gray-400 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"></path>
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"></path>
                                        </svg>
                                        <div class="flex-1">
                                            <p class="text-xs text-gray-500 dark:text-gray-400 uppercase font-semibold mb-1">Ubicación</p>
                                            <p class="text-sm font-medium text-gray-900 dark:text-white">
                                                {{ item.ubicacion_infraccion || 'No registrada' }}
                                            </p>
                                        </div>
                                    </div>
                                </div>

                                <div v-if="item.articulo_id" class="bg-amber-50 dark:bg-amber-900/20 border-l-4 border-amber-400 dark:border-amber-500 p-4 rounded-r-lg">
                                    <div class="flex items-start justify-between mb-2">
                                        <div class="flex items-center gap-2">
                                            <svg class="w-5 h-5 text-amber-600 dark:text-amber-400" fill="currentColor" viewBox="0 0 20 20">
                                                <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd"></path>
                                            </svg>
                                            <p class="text-xs text-amber-800 dark:text-amber-300 uppercase font-bold">Fundamento Legal</p>
                                        </div>
                                        <span class="inline-flex items-center px-2 py-1 rounded-md bg-amber-200 dark:bg-amber-800 text-amber-900 dark:text-amber-200 text-xs font-bold">
                                            Art. {{ item.articulo_id.articulo_numero }}
                                        </span>
                                    </div>
                                    <p class="text-sm font-semibold text-gray-900 dark:text-white mb-2">
                                        {{ item.articulo_id.ordenamiento }}
                                    </p>
                                    <p v-if="item.articulo_id.contenido" class="text-xs text-gray-700 dark:text-gray-300 italic leading-relaxed">
                                        "{{ item.articulo_id.contenido }}"
                                    </p>
                                </div>
                            </div>

                            <!-- Columna Derecha: Imágenes (MODIFICADO) -->
                            <div class="bg-gray-50 dark:bg-gray-900/50 p-6 lg:w-80 border-t lg:border-t-0 lg:border-l border-gray-200 dark:border-gray-700 space-y-6">

                                <!-- Sección Evidencia -->
                                <div>
                                    <div class="flex items-center gap-2 mb-3">
                                        <svg class="w-4 h-4 text-gray-500 dark:text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z" clip-rule="evenodd"></path>
                                        </svg>
                                        <p class="text-xs font-bold text-gray-700 dark:text-gray-300 uppercase">Evidencia Fotográfica</p>
                                    </div>
                                    <div v-if="item.evidencia_infraccion && item.evidencia_infraccion.length > 0" class="grid grid-cols-2 gap-3">
                                        <template v-for="img in item.evidencia_infraccion" :key="img.id">
                                            <div
                                                v-if="getImagenUrl(img.url)"
                                                class="relative group cursor-pointer overflow-hidden rounded-lg border-2 border-gray-200 dark:border-gray-600 hover:border-red-500 dark:hover:border-red-400 transition-all duration-200 bg-gray-200 dark:bg-gray-700"
                                                @click="abrirZoom(getImagenUrl(img.url))"
                                            >
                                                <!-- Skeleton Loader (Fondo pulsante mientras carga) -->
                                                <div
                                                    v-if="!imagenesCargadas[img.id]"
                                                    class="absolute inset-0 bg-gray-300 dark:bg-gray-600 animate-pulse z-10"
                                                ></div>

                                                <!-- Imagen Optimizada -->
                                                <img
                                                    :src="getImagenUrl(img.url)"
                                                    loading="lazy"
                                                    decoding="async"
                                                    class="h-24 w-full object-cover group-hover:scale-110 transition-all duration-500"
                                                    :class="{ 'opacity-0': !imagenesCargadas[img.id], 'opacity-100': imagenesCargadas[img.id] }"
                                                    alt="Evidencia"
                                                    @load="marcarImagenComoCargada(img.id)"
                                                />

                                                <div class="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-30 transition-opacity duration-200 flex items-center justify-center z-20">
                                                    <svg class="w-6 h-6 text-white opacity-0 group-hover:opacity-100 transition-opacity duration-200" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v6m3-3H7"></path>
                                                    </svg>
                                                </div>
                                            </div>
                                        </template>
                                    </div>
                                    <div v-else class="flex items-center justify-center h-24 border-2 border-dashed border-gray-300 dark:border-gray-600 rounded-lg">
                                        <p class="text-xs text-gray-400 dark:text-gray-500 italic">Sin fotos adjuntas</p>
                                    </div>
                                </div>

                                <!-- Sección Firma -->
                                <div>
                                    <div class="flex items-center gap-2 mb-3">
                                        <svg class="w-4 h-4 text-gray-500 dark:text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd" d="M6.267 3.455a3.066 3.066 0 001.745-.723 3.066 3.066 0 013.976 0 3.066 3.066 0 001.745.723 3.066 3.066 0 012.812 2.812c.051.643.304 1.254.723 1.745a3.066 3.066 0 010 3.976 3.066 3.066 0 00-.723 1.745 3.066 3.066 0 01-2.812 2.812 3.066 3.066 0 00-1.745.723 3.066 3.066 0 01-3.976 0 3.066 3.066 0 00-1.745-.723 3.066 3.066 0 01-2.812-2.812 3.066 3.066 0 00-.723-1.745 3.066 3.066 0 010-3.976 3.066 3.066 0 00.723-1.745 3.066 3.066 0 012.812-2.812zm7.44 5.252a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"></path>
                                        </svg>
                                        <p class="text-xs font-bold text-gray-700 dark:text-gray-300 uppercase">Firma Infractor</p>
                                    </div>
                                    <div
                                        v-if="item.firma_infractor && item.firma_infractor.url"
                                        class="bg-white dark:bg-gray-800 p-4 rounded-lg border-2 border-gray-200 dark:border-gray-600 cursor-pointer hover:border-red-500 dark:hover:border-red-400 hover:shadow-lg transition-all duration-200 relative min-h-[6rem]"
                                        @click="abrirZoom(getImagenUrl(item.firma_infractor.url))"
                                    >
                                        <!-- Skeleton Loader para Firma -->
                                        <div
                                            v-if="!imagenesCargadas['firma-'+item.id]"
                                            class="absolute inset-0 m-4 bg-gray-200 dark:bg-gray-700 rounded animate-pulse"
                                        ></div>

                                        <img
                                            :src="getImagenUrl(item.firma_infractor.url)"
                                            loading="lazy"
                                            decoding="async"
                                            class="h-16 mx-auto object-contain transition-opacity duration-500"
                                            :class="{ 'opacity-0': !imagenesCargadas['firma-'+item.id], 'opacity-100': imagenesCargadas['firma-'+item.id] }"
                                            alt="Firma"
                                            @load="marcarImagenComoCargada('firma-'+item.id)"
                                        />
                                    </div>
                                    <div v-else class="flex items-center justify-center h-16 bg-white dark:bg-gray-800 border-2 border-dashed border-gray-300 dark:border-gray-600 rounded-lg">
                                        <p class="text-xs text-gray-400 dark:text-gray-500 italic">No firmada</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Notificación Toast (Sin cambios) -->
            <Transition name="slide-down">
                <div
                    v-if="mostrarNotificacion"
                    class="fixed top-4 left-1/2 transform -translate-x-1/2 z-50 w-full max-w-md px-4"
                >
                    <div class="bg-green-500 dark:bg-green-600 text-white rounded-lg shadow-2xl p-4 flex items-center gap-3 border border-green-400 dark:border-green-500">
                        <div class="flex-shrink-0">
                            <svg class="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"></path>
                            </svg>
                        </div>
                        <div class="flex-1">
                            <p class="font-bold text-sm">¡Folio copiado!</p>
                            <p class="text-xs text-green-100 mt-0.5">
                                El folio <span class="font-semibold">{{ folioCopiado }}</span> se copió al portapapeles
                            </p>
                        </div>
                        <button
                            @click="mostrarNotificacion = false"
                            class="flex-shrink-0 p-1 hover:bg-green-600 dark:hover:bg-green-700 rounded transition-colors"
                        >
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                            </svg>
                        </button>
                    </div>
                </div>
            </Transition>

            <!-- Modal Zoom Imagen (Sin cambios) -->
            <div v-if="imagenSeleccionada" class="fixed inset-0 z-50 flex items-center justify-center bg-black/95 backdrop-blur-sm p-4 animate-fadeIn" @click.self="cerrarZoom">
                <button
                    @click="cerrarZoom"
                    class="absolute top-4 right-4 p-2 rounded-full bg-white/10 hover:bg-white/20 text-white transition-colors duration-200 group"
                    aria-label="Cerrar"
                >
                    <svg class="w-8 h-8 group-hover:rotate-90 transition-transform duration-200" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                    </svg>
                </button>
                <img
                    :src="imagenSeleccionada"
                    class="max-w-full max-h-full rounded-lg shadow-2xl object-contain"
                    alt="Zoom"
                />
            </div>
        </div>
    </PublicLayout>
</template>

<style scoped>
@keyframes fadeIn {
    from { opacity: 0; }
    to { opacity: 1; }
}

.animate-fadeIn {
    animation: fadeIn 0.2s ease-in-out;
}

.slide-down-enter-active {
    animation: slideDown 0.4s ease-out;
}

.slide-down-leave-active {
    animation: slideUp 0.4s ease-in;
}

@keyframes slideDown {
    from { transform: translate(-50%, -100%); opacity: 0; }
    to { transform: translate(-50%, 0); opacity: 1; }
}

@keyframes slideUp {
    from { transform: translate(-50%, 0); opacity: 1; }
    to { transform: translate(-50%, -100%); opacity: 0; }
}
</style>
