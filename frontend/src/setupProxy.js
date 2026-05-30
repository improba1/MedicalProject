// frontend/src/setupProxy.js
const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
    // Proxy dla AI Assistant
    app.use(
        '/assistant',
        createProxyMiddleware({
            target: 'http://localhost:8000', // Docker DNS //changed from http://assistant:8000
            changeOrigin: true,
            pathRewrite:{
                '^/assistant' : '',
            }
        })
    );

    app.use(
        '/api',
        createProxyMiddleware({
            target: 'http://localhost:8080',
            changeOrigin: true
        })
    )
};
