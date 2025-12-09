Feature: Add Pet to store

  Background: conditional_init
    But Conditional_Environment_SetUp

  @Sanity
  Scenario Outline: Add new pet to store's catalog with valid pet details

    Given user_provides_information('<Unique_Test_Case_ID>','<SheetName>')
    When user_calls_addPet_Service
    Then addPet_service_returns_code_<ExpectedResponseCode>
    And addPet_service_returns_response_body

    Examples:
      |Unique_Test_Case_ID                                   |SheetName|ExpectedResponseCode|
      |TC_01_Add_Pet_To_Store_XML_input_AllFieldsProvided    |ADD_PET  |200				  |
      |TC_02_Add_Pet_To_Store_Json_input_AllFieldsProvided   |ADD_PET  |200				  |
      |TC_03_Add_Pet_To_Store_XML_WithOnlyMandatoryFields    |ADD_PET  |200				  |
      |TC_04_Add_Pet_To_Store_JSON_WithOnlyMandatoryFields   |ADD_PET  |200				  |
      |TC_05_Add_Pet_To_Store_XML_WithMultiplePicsAndTags    |ADD_PET  |200				  |
      |TC_06_Add_Pet_To_Store_JSON_WithMultiplePicsAndTags   |ADD_PET  |200				  |

  @Regression
  Scenario Outline: Add new pet to store's catalog with Invalid pet details

    Given user_provides_information('<Unique_Test_Case_ID>','<SheetName>')
    When user_calls_addPet_Service
    Then addPet_service_returns_code_<ExpectedResponseCode>

    #### Sample Negative cases
    ### Test missing headers,incorrect header details; schema validation- mandatory tags, edge cases
    ### Pet status is enum [ available, pending, sold ], anything else should result in 405-Invalid input etc.
    Examples:
      |Unique_Test_Case_ID                                            |SheetName|ExpectedResponseCode|
      |TC_07_Add_Pet_Json_Invalid_Details_MissingHeader_accept        |ADD_PET  |400                 |
      |TC_08_Add_Pet_Json_Invalid_Details_MissingHeader_content-type  |ADD_PET  |400                 |
      |TC_09_Add_Pet_Json_Invalid_Details_wrong_PetStatus             |ADD_PET  |405                 |
