(function(angular, $) {
    'use strict';
    angular.module('FileManager').service('apiHandler', ['$http', '$q', '$window', '$translate',
        function ($http, $q, $window, $translate) {

        $http.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
        $http.defaults.headers.common['API_BASE'] = 'Rest';


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

        ApiHandler.prototype.setApiBase = function(API_BASE) {
            $http.defaults.headers.common['API_BASE'] = API_BASE;
        };

        ApiHandler.prototype.list = function(apiUrl, path, customDeferredHandler) {
            var self = this;
            var dfHandler = customDeferredHandler || self.deferredHandler;
            var deferred = $q.defer();

            var data = {
                action: 'list',
                path: path
            };

            self.inprocess = true;
            self.error = '';

            $http.post(apiUrl, data).success(function(data) {
                dfHandler(data, deferred);
            }).error(function(data) {
                dfHandler(data, deferred, 'Unknown error listing, check the response');
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.copy = function(apiUrl, items, path) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'copy',
                items: items,
                newPath: path
            };

            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_copying'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.move = function(apiUrl, items, path) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'move',
                items: items,
                newPath: path
            };
            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_moving'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.remove = function(apiUrl, items) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'remove',
                items: items
            };

            self.inprocess = true;
            self.error = '';
            //$http.get(apiUrl+"/"+folderId) ;
            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_deleting'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.upload = function(apiUrl, form) {
            var self = this;
            var deferred = $q.defer();
            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, form, {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined
                }
            }).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, 'Unknown error uploading files');
            })['finally'](function() {
                self.inprocess = false;
            });

            return deferred.promise;
        };

        ApiHandler.prototype.getContent = function(apiUrl, itemPath) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'getContent',
                item: itemPath
            };

            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_getting_content'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.edit = function(apiUrl, itemPath, content) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'edit',
                item: itemPath,
                content: content
            };

            self.inprocess = true;
            self.error = '';

            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_modifying'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.rename = function(apiUrl, itemPath, newPath) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'rename',
                item: itemPath,
                newItemPath: newPath
            };
            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_renaming'));
            })['finally'](function() {
                self.inprocess = false;
            });
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
            self.inprocess = true;
            $http.get(url).success(function(data) {
                var bin = new $window.Blob([data]);
                deferred.resolve(data);
                $window.saveAs(bin, toFilename);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_downloading'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.downloadMultiple = function(apiUrl, items, toFilename, downloadByAjax, forceNewWindow) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'downloadMultiple',
                items: items,
                toFilename: toFilename
            };
            var url = [apiUrl, $.param(data)].join('?');

            if (!downloadByAjax || forceNewWindow || !$window.saveAs) {
                !$window.saveAs && $window.console.error('Your browser dont support ajax download, downloading by default');
                return !!$window.open(url, '_blank', '');
            }

            self.inprocess = true;
            $http.get(apiUrl).success(function(data) {
                var bin = new $window.Blob([data]);
                deferred.resolve(data);
                $window.saveAs(bin, toFilename);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_downloading'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.compress = function(apiUrl, items, compressedFilename, path) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'compress',
                items: items,
                destination: path,
                compressedFilename: compressedFilename
            };

            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_compressing'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.extract = function(apiUrl, item, folderName, path) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'extract',
                item: item,
                destination: path,
                folderName: folderName
            };

            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_extracting'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.changePermissions = function(apiUrl, items, permsOctal, permsCode, recursive) {
            var self = this;
            var deferred = $q.defer();
            var data = {
                action: 'changePermissions',
                items: items,
                perms: permsOctal,
                permsCode: permsCode,
                recursive: !!recursive
            };

            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_changing_perms'));
            })['finally'](function() {
                self.inprocess = false;
            });
            return deferred.promise;
        };

        ApiHandler.prototype.createFolder = function(apiUrl, item) {
            var self = this;
            var deferred = $q.defer();
            //console.log(item.id + " " + item.tempModel.id);
            var data = {
                action: 'createFolder',
                newPath: item.path,
                name: item.tempModel.name,
                parentFolderId: item.tempModel.id
            };

            self.inprocess = true;
            self.error = '';
            $http.post(apiUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, $translate.instant('error_creating_folder'));
            })['finally'](function() {
                self.inprocess = false;
            });

            return deferred.promise;
        };

        return ApiHandler;

    }]);
})(angular, jQuery);
