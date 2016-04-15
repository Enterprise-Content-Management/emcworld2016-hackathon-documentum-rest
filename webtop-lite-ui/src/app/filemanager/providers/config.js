(function(angular) {
    'use strict';
    angular.module('FileManager')
    .constant('SPRING_API', 'http://localhost:7000')
    .provider('fileManagerConfig', function(SPRING_API) {

        var values = {
            appName: 'ECD File Manager',
            defaultLang: 'en',

            listUrl: SPRING_API + '/api/listUrl',
            uploadUrl: SPRING_API + '/api/uploadUrl',
            renameUrl: SPRING_API + '/api/renameUrl',
            copyUrl: SPRING_API + '/api/copyUrl',
            moveUrl: SPRING_API + '/api/moveUrl',
            removeUrl: SPRING_API + '/api/deleteFolderUrl',
            editUrl: SPRING_API + '/api/editUrl',
            getContentUrl: SPRING_API + '/api/document/open',
            createFolderUrl: SPRING_API + '/api/createFolderUrl',
            downloadMultipleUrl: SPRING_API + '/api/document/content',
            downloadFileUrl: SPRING_API + '/api/document/content',
            compressUrl: SPRING_API + '/api/compressUrl',
            extractUrl: SPRING_API + '/api/extractUrl',
            permissionsUrl: SPRING_API + '/api/permissionsUrl',

            searchForm: true,
            sidebar: true,
            breadcrumb: true,
            allowedActions: {
                upload: true,
                rename: true,
                copy: true,
                move:true,
                edit: true,
                changePermissions: true,
                compress: true,
                compressChooseName: true,
                extract: true,
                download: true,
                preview: true,
                remove: true
            },

            showSizeForDirectories: false,
            hideDate: false,
            hidePermissions: false,
            useBinarySizePrefixes: false,

            previewImagesInModal: true,
            enablePermissionsRecursive: true,
            compressAsync: true,
            extractAsync: true,

            isEditableFilePattern: /\.(txt|html?|aspx?|ini|pl|py|md|css|js|log|htaccess|htpasswd|json|sql|xml|xslt?|sh|rb|as|bat|cmd|coffee|php[3-6]?|java|c|cbl|go|h|scala|vb)$/i,
            isImageFilePattern: /\.(jpe?g|gif|bmp|png|svg|tiff?)$/i,
            isExtractableFilePattern: /\.(gz|tar|rar|g?zip)$/i,
            isPdfFilePattern: /\.(pdf)$/i,
            tplPath: 'app/filemanager/templates'
        };

        return {
            $get: function() {
                return values;
            },
            set: function (constants) {
                angular.extend(values, constants);
            }
        };

    });
})(angular);
