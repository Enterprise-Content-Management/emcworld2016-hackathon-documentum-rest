(function(angular) {
    'use strict';
    angular.module('DocumentumNgFileManager').factory('item', ['fileManagerConfig', 'chmod', function(fileManagerConfig, Chmod) {

        var Item = function(model, path) {
            var rawModel = {
                id : model && model.id || '',
                name: model && model.name || '',
                path: path || [],
                type: model && model.type || 'file',
                size: model && parseInt(model.size || 0),
                date: model && model.date || '', 
                perms: new Chmod(model && model.rights),
                content: model && model.content || '',
                recursive: false,
                fullPath: function() {
                    var path = this.path.filter(Boolean);
                    return ('/' + path.join('/') + '/' + this.name).replace(/\/\//, '/');
                }
            };

            this.error = '';
            this.processing = false;

            this.model = angular.copy(rawModel);
            this.tempModel = angular.copy(rawModel);
        };

        Item.prototype.update = function() {
            angular.extend(this.model, angular.copy(this.tempModel));
        };

        Item.prototype.revert = function() {
            angular.extend(this.tempModel, angular.copy(this.model));
            this.error = '';
        };

        Item.prototype.isFolder = function() {
            return this.model.type === 'dir';
        };

        Item.prototype.isEditable = function() {
            return !this.isFolder() && fileManagerConfig.isEditableFilePattern.test(this.model.name);
        };

        Item.prototype.isImage = function() {
            return fileManagerConfig.isImageFilePattern.test(this.model.name);
        };

        Item.prototype.isPdf = function() {
            return fileManagerConfig.isPdfFilePattern.test(this.model.name);
        };

        Item.prototype.isCompressible = function() {
            return this.isFolder();
        };

        Item.prototype.isExtractable = function() {
            return !this.isFolder() && fileManagerConfig.isExtractableFilePattern.test(this.model.name);
        };

        return Item;
    }]);
})(angular);
