(function() {
  'use strict';

  angular
    .module('pdfviewer',['pdfjsViewer'])
    .controller('pdfViewerController', pdfViewerController);

  /** @ngInject */
  function pdfViewerController($scope, pdfBlobUrl) {
    $scope.pdf = {
      src: pdfBlobUrl
    }
  }

})();