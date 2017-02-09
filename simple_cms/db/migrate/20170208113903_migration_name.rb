class MigrationName < ActiveRecord::Migration
  def change
add_column :pictures, :inventory_id, :integer

  end
end
