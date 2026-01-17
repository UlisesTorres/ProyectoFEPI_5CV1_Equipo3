export default [
  'strapi::logger',
  'strapi::errors',
  'strapi::security',
  'strapi::cors',
  'strapi::poweredBy',
  'strapi::query',

  {
    name: 'strapi::body',
    config: {
      jsonLimit: '50mb',
      formLimit: '50mb',
      textLimit: '50mb',
      urlencodedLimit: '50mb',
      multipart: true,
    },
  },

  'strapi::session',
  'strapi::favicon',
  'strapi::public',
];
