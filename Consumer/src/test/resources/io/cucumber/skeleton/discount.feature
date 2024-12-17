Feature: Update Prices with one Product

  # This Test does not actually rely on the ProductService but still as it is mocked
  # It's interaction is recorded
  Scenario: Set Discount
    When Discount is changed to 10%
    Then Discount should be 10%

  # Here we have several interactions
  Scenario: Update Prices with one Product
    # we are defining a Product which will be returned
    Given A Product A
    And Product A has id 1
    And Product A has price 20€
    And Product A is stored
    # Here we'll get all Products and discount the price on each of them.
    When Discount is changed to 10%
    # Now we verify that the new price is correctly returned by the provider and mock.
    Then Product A should have price 18€
