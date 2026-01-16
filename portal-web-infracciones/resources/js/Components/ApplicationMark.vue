<template>
    <img :src="logoUrl" alt="Logo Portal de Infracciones" :class="$attrs.class">
</template>

<script setup>
defineOptions({
    inheritAttrs: false,
});

const logoUrl = '/images/logo.png';
</script>
