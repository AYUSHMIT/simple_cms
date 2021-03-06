class PicturesController < ApplicationController
  before_action :set_picture, only: [:show, :edit, :update, :destroy]

  # GET /pictures
  # GET /pictures.json
  def index
    puts "-----Hi i am here---------"
    @pictures = Picture.all
  end

  # GET /pictures/1
  # GET /pictures/1.json
  def show
    @picture = Picture.find(params[:id])
    #send_file(@picture.image.url, disposition: 'picture')
    redirect_to @picture.image.url
    puts "----------params---------#{@picture.image.url.inspect}"
  end

  # GET /pictures/new
  def new
    @picture = Picture.new
  end

  # GET /pictures/1/edit
  def edit
  end

  # POST /pictures
  # POST /pictures.json
  def create
    puts "----------params---------#{params.inspect}"
    puts "\n\n\n\n"

    @picture = Picture.new()

    #@picture.id=1070

     @picture.image = params["image"]
     @picture.nam = params["myString"]
     @picture.inventory_id =  params["inventory_id"]

     #@picture.name = params["myString"]

    puts "-------------------#{@picture.nam.inspect}"
    #s @picture.first_name = params["myString"]
  #  @picture.image = @picture.image.force_encoding("UTF-8")

 # puts "-----------------@first_name------------#{@picture.first_name.inspect}"

    puts "-----------------@picture------------#{@picture.image.inspect}"

    @picture.save

    respond_to do |format|
      if @picture.save
        format.html { redirect_to @picture, notice: 'Picture was successfully created.' }
        format.json { render :show, status: :created, location: @picture }
      else
        format.html { render :new }
        format.json { render json: @picture.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /pictures/1
  # PATCH/PUT /pictures/1.json
  def update
    respond_to do |format|
      if @picture.update(picture_params)
        format.html { redirect_to @picture, notice: 'Picture was successfully updated.' }
        format.json { render :show, status: :ok, location: @picture }
      else
        format.html { render :edit }
        format.json { render json: @picture.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /pictures/1
  # DELETE /pictures/1.json
  def destroy
    @picture.destroy
    respond_to do |format|
      format.html { redirect_to pictures_url, notice: 'Picture was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_picture
      @picture = Picture.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def picture_params
      params.require(:params).permit(:image, :myString)
    end
end
