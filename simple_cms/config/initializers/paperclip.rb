require 'paperclip/media_type_spoof_detector'
module Paperclip
  class MediaTypeSpoofDetector
    def spoofed?
      false
    end
  end
end

Paperclip.interpolates :name  do |attachment, style|
  attachment.instance.nam
end




#Paperclip::Attachment.interpolations[:nam] = proc do |attachment, style|
  # or whatever you've named your User's login/username/etc. attribute
 # attachment.instance.nam
#end


