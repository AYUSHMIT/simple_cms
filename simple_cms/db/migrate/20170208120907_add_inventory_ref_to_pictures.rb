class AddInventoryRefToPictures < ActiveRecord::Migration
  def change
add_reference :pictures, :inventory, index: true, foreign_key: true


  end
end
