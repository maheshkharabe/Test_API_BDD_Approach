Feature: Get Pet details from store

  Background: conditional_init
    But Conditional_Environment_SetUp

  @Sanity
  Scenario Outline: Retrieve pet details from store with valid data

    Given user_provides_pet_information('<Unique_Test_Case_ID>','<SheetName>')
    When user_calls_findPetByID_Service
    Then getPet_service_returns_<ExpectedResponseCode>
    And user_receives_pet_data_in_response_body

    Examples:
      |Unique_Test_Case_ID                                |SheetName|ExpectedResponseCode|
      |TC_01_Pet_In_Store_XML_input_AllFieldsProvided     |GET_PET  |200                 |
      |TC_02_Pet_In_Store_Json_input_AllFieldsProvided    |GET_PET  |200                 |

  @Regression
  Scenario Outline: Retrieve pet details from store with Invalid data

    Given user_provides_pet_information('<Unique_Test_Case_ID>','<SheetName>')
    When user_calls_findPetByID_Service
    Then getPet_service_returns_<ExpectedResponseCode>
    And user_received_feedback_message_for_invalid_data('<expRespBodyCode>','<expRespBodyStdType>','<expRespBodyStdMessage>')


    Examples:
      |Unique_Test_Case_ID                                      |SheetName|ExpectedResponseCode|expRespBodyCode|expRespBodyStdType|expRespBodyStdMessage|
      |TC_03_Pet_In_Store_XML_with_invalid_PetID_doesnt_exist   |GET_PET  |404                 |1              |error             |Pet not found        |
      |TC_04_Pet_In_Store_JSON_with_invalid_PetID_doesnt_exist  |GET_PET  |404                 |1              |error             |Pet not found        |
