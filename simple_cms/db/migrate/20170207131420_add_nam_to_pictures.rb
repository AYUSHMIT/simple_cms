class AddNamToPictures < ActiveRecord::Migration
  def change
    add_column :pictures, :nam, :string
  end
end
