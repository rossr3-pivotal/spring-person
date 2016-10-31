angular.module('persons', ['ngResource', 'ui.bootstrap']).
    factory('Persons', function ($resource) {
    	console.log("returning the resource for persons: " + $resource);
        return $resource('persons');
    }).
    factory('Person', function ($resource) {
    	console.log("returning the person factory " + $resource);
        return $resource('persons/:emailAddress/', {emailAddress: '@emailAddress'});
    }).
    factory("EditorStatus", function () {
        var editorEnabled = {};

        var enable = function (id, fieldName) {
            editorEnabled = { 'id': id, 'fieldName': fieldName };
        };

        var disable = function () {
            editorEnabled = {};
        };

        var isEnabled = function(id, fieldName) {
            return (editorEnabled['id'] == id && editorEnabled['fieldName'] == fieldName);
        };

        return {
            isEnabled: isEnabled,
            enable: enable,
            disable: disable
        }
    });

function PersonsController($scope, $modal, Persons, Person, Status) {
    function list() {
    	console.log("Retrieving the list of persons...");
        $scope.persons = Persons.query();
    }

    function clone (obj) {
        return JSON.parse(JSON.stringify(obj));
    }

    function savePerson(person) {
        Persons.save(person,
            function () {
                Status.success("Person saved");
                list();
            },
            function (result) {
                Status.error("Error saving person: " + result.status);
            }
        );
    }

    $scope.addPerson = function () {
        var addModal = $modal.open({
            templateUrl: 'templates/personForm.html',
            controller: PersonModalController,
            resolve: {
                person: function () {
                    return {};
                },
                action: function() {
                    return 'add';
                }
            }
        });

        addModal.result.then(function (person) {
            savePerson(person);
        });
    };

    $scope.updatePerson = function (person) {
        var updateModal = $modal.open({
            templateUrl: 'templates/personForm.html',
            controller: PersonModalController,
            resolve: {
                person: function() {
                    return clone(person);
                },
                action: function() {
                    return 'update';
                }
            }
        });

        updateModal.result.then(function (person) {
            savePerson(person);
        });
    };

    $scope.deletePerson = function (person) {
    	console.log("deletePerson: person email address is " + person.emailAddress + " end of message");
        Person.delete({emailAddress: person.emailAddress},
            function () {
                Status.success("Person deleted");
                list();
            },
            function (result) {
                Status.error("Error deleting person: " + result.status);
                console.log(result);
            }
        );
    };

    $scope.setPersonsView = function (viewName) {
        $scope.personsView = "templates/" + viewName + ".html";
    };

    $scope.init = function() {
        list();
        $scope.setPersonsView("grid");
        $scope.sortField = "name";
        $scope.sortDescending = false;
    };
}

function PersonModalController($scope, $modalInstance, person, action) {
    $scope.personAction = action;
    $scope.person = person;

    $scope.ok = function () {
        $modalInstance.close($scope.person);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

function PersonEditorController($scope, Persons, Status, EditorStatus) {
    $scope.enableEditor = function (person, fieldName) {
        $scope.newFieldValue = person[fieldName];
        EditorStatus.enable(person.id, fieldName);
    };

    $scope.disableEditor = function () {
        EditorStatus.disable();
    };

    $scope.isEditorEnabled = function (person, fieldName) {
        return EditorStatus.isEnabled(person.id, fieldName);
    };

    $scope.save = function (person, fieldName) {
        if ($scope.newFieldValue === "") {
            return false;
        }

        person[fieldName] = $scope.newFieldValue;

        Persons.save({}, person,
            function () {
                Status.success("Person saved");
                list();
            },
            function (result) {
                Status.error("Error saving person: " + result.status);
            }
        );

        $scope.disableEditor();
    };

    $scope.disableEditor();
}

angular.module('persons').
    directive('inPlaceEdit', function () {
        return {
            restrict: 'E',
            transclude: true,
            replace: true,

            scope: {
                ipeFieldName: '@fieldName',
                ipeInputType: '@inputType',
                ipeInputClass: '@inputClass',
                ipePattern: '@pattern',
                ipeModel: '=model'
            },

            template:
                '<div>' +
                    '<span ng-hide="isEditorEnabled(ipeModel, ipeFieldName)" ng-click="enableEditor(ipeModel, ipeFieldName)">' +
                        '<span ng-transclude></span>' +
                    '</span>' +
                    '<span ng-show="isEditorEnabled(ipeModel, ipeFieldName)">' +
                        '<div class="input-append">' +
                            '<input type="{{ipeInputType}}" name="{{ipeFieldName}}" class="{{ipeInputClass}}" ' +
                                'ng-required ng-pattern="{{ipePattern}}" ng-model="newFieldValue" ' +
                                'ui-keyup="{enter: \'save(ipeModel, ipeFieldName)\', esc: \'disableEditor()\'}"/>' +
                            '<div class="btn-group btn-group-xs" role="toolbar">' +
                                '<button ng-click="save(ipeModel, ipeFieldName)" type="button" class="btn"><span class="glyphicon glyphicon-ok"></span></button>' +
                                '<button ng-click="disableEditor()" type="button" class="btn"><span class="glyphicon glyphicon-remove"></span></button>' +
                            '</div>' +
                        '</div>' +
                    '</span>' +
                '</div>',

            controller: 'PersonEditorController'
        };
    });
