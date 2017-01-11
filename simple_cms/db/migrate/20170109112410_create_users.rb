class CreateUsers < ActiveRecord::Migration
  def up
    create_table :users do |t|
      t.column "first_name", :string, :limit => 25
      t.string "last_name", :limit => 50
      
    end
  end

  def down
  	drop_table :users
  end
end
