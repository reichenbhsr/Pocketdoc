module.exports = function(config){
  config.set({

    basePath : '../',

    files : [
      'bower_components/jquery/dist/jquery.min.js',
      'bower_components/underscore/underscore-min.js',
      'bower_components/angular/angular.js',
      'bower_components/angular-route/angular-route.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-mocks/angular-mocks.js',
      'bower_components/angular-aria/angular-aria.min.js',
      'bower_components/angular-cookies/angular-cookies.min.js',
      'bower_components/angular-translate/angular-translate.min.js',
      'bower_components/angular-material/angular-material.min.js',
      'bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min.js',
      'bower_components/angular-messages/angular-messages.min.js',
      'js/data.js',
      'js/simulator.js',
      'js/controller.js',
      'js/factories.js',
      'js/services.js',
      'js/app.js',
      'test/unit/**/*.js'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine'
            ],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};