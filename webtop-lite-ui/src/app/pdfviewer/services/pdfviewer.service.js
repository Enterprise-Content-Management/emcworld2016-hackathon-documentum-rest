(function(angular) {
    'use strict';
    angular.module('pdfviewer')
    .service('pdfService', ['$http', '$q', 'SPRING_API', function ($http, $q, SPRING_API) {
        var self  = {
            'base64ToBlob': function (b64Data, contentType, sliceSize) {
                contentType = contentType || '';
                sliceSize = sliceSize || 512;

                var byteCharacters = atob(b64Data);
                var byteArrays = [];

                for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
                    var slice = byteCharacters.slice(offset, offset + sliceSize);

                    var byteNumbers = new Array(slice.length);
                    for (var i = 0; i < slice.length; i++) {
                        byteNumbers[i] = slice.charCodeAt(i);
                    }

                    var byteArray = new Uint8Array(byteNumbers);

                    byteArrays.push(byteArray);
                }

                var blob = new Blob(byteArrays, {type: contentType});
                return blob;
            },
            'openDocument': function(id){
                var deferred = $q.defer();
                $http.get(SPRING_API + '/api/document/open/' + id)
                .success(function(response){
                    var pdfContent = response.data,
                        contentType = 'data:application/pdf;base64, ',
                        blob = self.base64ToBlob(pdfContent, contentType),
                        blobUrl = URL.createObjectURL(blob);
                    
                    deferred.resolve(blobUrl);
                })
                .error(function(err){
                  deferred.reject(err);
                });

                return deferred.promise;
            }
        };

        return self;
    }]);
})(angular);