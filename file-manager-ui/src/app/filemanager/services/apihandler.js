(function(angular, $) {
    'use strict';
    angular.module('DocumentumNgFileManager').service('apiHandler', ['$http', '$q', '$window', '$translate',
        function ($http, $q, $window, $translate) {

        $http.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

        var ApiHandler = function() {
            this.inprocess = false;
            this.asyncSuccess = false;
            this.error = '';
        };

        ApiHandler.prototype.deferredHandler = function(data, deferred, defaultMsg) {
            if (!data || typeof data !== 'object') {
                this.error = 'Bridge response error, please check the docs';
            }
            if (data.result && data.result.error) {
                this.error = data.result.error;
            }
            if (!this.error && data.error) {
                this.error = data.error.message;
            }
            if (!this.error && defaultMsg) {
                this.error = defaultMsg;
            }
            if (this.error) {
                return deferred.reject(data);
            }
            return deferred.resolve(data);
        };

        ApiHandler.prototype.list = function(apiUrl, path, customDeferredHandler) {
            var self = this;
            var dfHandler = customDeferredHandler || self.deferredHandler;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.copy = function(apiUrl, ids, path) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.move = function(apiUrl, ids, path) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.remove = function(apiUrl, ids) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.upload = function(apiUrl, form) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.getContent = function(apiUrl) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.edit = function(apiUrl, id, content) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.rename = function(apiUrl, itemPath, newPath) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.getUrl = function(apiUrl, item) {
            return apiUrl + "/" + item.tempModel.id;
        };

        ApiHandler.prototype.download = function(apiUrl, item, toFilename, downloadByAjax, forceNewWindow) {
            var self = this;
            var url = this.getUrl(apiUrl, item);
            //!$window.saveAs && $window.console.error('Your browser dont support ajax download, downloading by default');
            window.open(url, '_blank', '');
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.downloadMultiple = function(apiUrl, ids, toFilename, downloadByAjax, forceNewWindow) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.compress = function(apiUrl, ids, compressedFilename, path) {
            var self = this;
            var deferred = $q.defer();
            
            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.extract = function(apiUrl, id, folderName, path) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.changePermissions = function(apiUrl, ids, permsOctal, permsCode, recursive) {
            var self = this;
            var deferred = $q.defer();
            
            // todo: implement here
            
            return deferred.promise;
        };

        ApiHandler.prototype.createFolder = function(apiUrl, item) {
            var self = this;
            var deferred = $q.defer();
            
            //todo - implement here

            return deferred.promise;
        };

        ApiHandler.prototype.ftSearch = function(apiUrl, item, path, pageNumber, pageSize) {
            var self = this;
            var deferred = $q.defer();

            // todo: implement here

            return deferred.promise;
        };

        return ApiHandler;

    }]);
})(angular, jQuery);
