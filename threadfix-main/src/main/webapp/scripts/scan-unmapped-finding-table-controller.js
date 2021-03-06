var myAppModule = angular.module('threadfix');

myAppModule.controller('ScanUnmappedFindingTableController', function ($scope, $window, $http, $rootScope, $modal, $log, tfEncoder) {

    $scope.initialized = false;

    $scope.page = 1;

    $scope.heading = "Unmapped Findings";

    $scope.$on('rootScopeInitialized', function() {

        $http.get(tfEncoder.encode($scope.$parent.currentUrl + "/cwe"), getTableSortBean()).
            success(function(data) {

                if (data.success) {
                    $scope.genericVulnerabilities = data.object;
                } else {
                    $scope.output = "Failure. Message was : " + data.message;
                }
            }).
            error(function(data, status) {
                $scope.errorMessage = "Failed to retrieve team list. HTTP status was " + status;
            });

        return $scope.refresh(true, false);
    });

    $scope.$on('scanUploaded', function() {
        $scope.refresh(true, false);
    });

    $scope.$on('scanDeleted', function() {
        $scope.refresh(true, false);
    });

    $scope.refresh = function(newValue, oldValue) {
        if (newValue !== oldValue) {
            $scope.loading = true;
            $http.post(tfEncoder.encode($scope.$parent.currentUrl + "/unmappedTable"), getTableSortBean()).
                success(function(data) {

                    if (data.success) {
                        $scope.numPages = data.object.numPages;
                        $scope.page = data.object.page;
                        $scope.numFindings = data.object.numFindings;
                        $scope.heading = $scope.numFindings + " Unmapped Findings";
                        $scope.numberOfUnmappedPages = Math.ceil(data.object.numFindings/100);
                        $scope.findingList = data.object.findingList;
                        $scope.scan = data.object.scan;
                    } else {
                        $scope.output = "Failure. Message was : " + data.message;
                    }

                    $scope.loading = false;
                }).
                error(function(data, status) {
                    $scope.errorMessage = "Failed to retrieve team list. HTTP status was " + status;
                    $scope.loading = false;
                });
        }
    };

    $scope.$watch('page', $scope.refresh);

    var getTableSortBean = function() {
        var object = {
            page: $scope.page
        };
        return object;
    };

    $scope.createMapping = function(finding) {
        var modalInstance = $modal.open({
            windowClass: 'mapping-filter-modal',
            templateUrl: 'createMappingModal.html',
            controller: 'ModalControllerWithConfig',
            resolve: {
                url: function() {
                    return tfEncoder.encode("/scannerMappings/update");
                },
                object: function () {
                    return {
                        channelVulnerabilityCode: finding.channelVulnerability.name,
                        channelName : finding.scannerName
                    };
                },
                config: function() {
                    return {
                        genericVulnerabilities: $scope.genericVulnerabilities,
                        finding: finding
                    }
                },
                buttonText: function() {
                    return "Create Mapping";
                }
            }
        });

        $scope.currentModal = modalInstance;

        modalInstance.result.then(function (object) {
            $scope.successMessage = "Successfully created mapping. You should re-upload your scan.";
            $scope.refresh(true, false);
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.goToPage = function(valid) {
        if (valid) {
            $scope.page = $scope.pageInput;
        }
    };

    $scope.goTo = function(finding) {

        var url = $scope.$parent.currentUrl.indexOf('scans') == -1 ?
            $scope.$parent.currentUrl + "/scans/1/findings/" + finding.id :
            $scope.$parent.currentUrl + "/findings/" + finding.id;

        $window.location.href = tfEncoder.encode(url);
    };

});