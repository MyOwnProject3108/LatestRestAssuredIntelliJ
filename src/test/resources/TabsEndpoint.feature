Feature: Validate Traffic Tab API

  @validUserGetTab
  Scenario Outline: Validate the responsecode of GET API Tab for TTM/BTM
    Given I logged in as "<user>"
    When I retrieve tabs for the user "<user>"
    Then I should see valid response code "200" with "OK"

    Examples:
      | user              |
      | Traffic Manager   |
      | Broadcast Manager |


  @InvalidUserGetTab
  Scenario Outline: Validate the responsecode of GET API Tab for Invalid User/UnAuthorised User
    Given I logged in as "<user>"
    When I retrieve tabs for the user "<user>"
    Then I should see below response code "<responsecode>" with "<statuscode>"

    Examples:
      | user            | responsecode | statuscode                                                            |
      | UnAuth User     |          403 | The supplied authentication is not authorized to access this resource |
      | NullAuthUser    |          400 | Request is missing required HTTP header 'X-User-Id'                   |
      | BTMUnAuth User  |          403 | The supplied authentication is not authorized to access this resource |
      | BTMNullAuthUser |          400 | Request is missing required HTTP header 'X-User-Id'                   |

  @ValidUserCreateTab
  Scenario Outline: Validate the responsecode for Create tab with POST method for valid user
    Given I logged in as "<user>"
    When I create a tab for the user "<user>" with the following payload
      | name            | public | default | tabType |
      | QA API OrderTab | false  | false   | Order   |
    Then I should see following details in Retrieve tabs response payload
      |name           |businessUnitId          |
      |QA API OrderTab|5b4476743e468338ff3e701a|

    Examples:
      | user              |
      | Traffic Manager   |


