class Picture < ActiveRecord::Base

belongs_to :inventory

	# attr_protected
	has_attached_file :image, :url => "/:name/:filename"
	#ssdefine_attribute 'first_name'
#validates_attachment :image, :content_type =>  { :content_type => "image/jpg" }
	#validates_attachment :image, content_type: { content_type: "image/png" }
	#validates_attachment :image#, content_type: { content_type: "application/octet-stream\r\n" }
	do_not_validate_attachment_file_type :image


end
			