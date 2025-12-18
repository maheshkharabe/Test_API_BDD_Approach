@Regression @Integration
Feature: Integration check for Add and get pet details

  Background: conditional_init

    But Conditional_Environment_SetUp

  Scenario Outline: Add new pet to store's catalog with valid pet details and check details are added with Get Pet service

    Given user_provides_information('<Unique_Test_Case_ID>','<SheetName>')
    When user_calls_addPet_Service
    Then addPet_service_returns_code_<ExpectedResponseCode>
    And addPet_service_returns_response_body
    And user_provides_pet_information('<Unique_Test_Case_ID>','GET_PET')
    When user_calls_findPetByID_Service
    Then getPet_service_returns_<ExpectedResponseCode>
    And user_receives_pet_data_in_response_body


    Examples:
      |Unique_Test_Case_ID                                   |SheetName|ExpectedResponseCode|
      |TC_01_Pet_In_Store_XML_input_AllFieldsProvided        |ADD_PET  |200				    |
      |TC_02_Pet_In_Store_Json_input_AllFieldsProvided       |ADD_PET  |200				    |
