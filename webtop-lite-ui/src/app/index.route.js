(function() {
  'use strict';

  angular
    .module('commonViewer')
    .config(routerConfig);

  /** @ngInject */
  function routerConfig($stateProvider, $urlRouterProvider, $sceDelegateProvider, $locationProvider) {

    // Browser Support?
    // if(window.history && window.history.pushState){
    //   $locationProvider.html5Mode({
    //           enabled: true,
    //           requireBase: false
    //    });
    // }

    // Trust Blob URL
    $sceDelegateProvider.resourceUrlWhitelist(['**']);

    $stateProvider
      .state('home', {
        url: '/',
        templateUrl: 'app/filemanager/templates/main.html',
        controller: 'FileManagerController',
      })
      .state('pdfviewer', {
        url: '/pdfviewer/:id',
        templateUrl: 'app/pdfviewer/templates/main.html',
        controller: 'pdfViewerController',
        resolve: {
          pdfBlobUrl: function ($stateParams, pdfService) {
            return pdfService.openDocument($stateParams.id).then(function(response){ return response; });
          }
        }
      });


    $urlRouterProvider.otherwise('/');
  }

})();
