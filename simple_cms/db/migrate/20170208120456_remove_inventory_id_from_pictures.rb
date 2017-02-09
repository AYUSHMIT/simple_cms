class RemoveInventoryIdFromPictures < ActiveRecord::Migration
  def change
 remove_column :pictures, :inventory_id, :integer


  end
end
