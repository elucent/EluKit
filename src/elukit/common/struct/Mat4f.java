package elukit.common.struct;

public class Mat4f {
	float[] data = new float[16];
	
	public Mat4f(float[] data){
		this.data = data;
	}
	
	public Mat4f(float val){
		for (int i = 0; i < 16; i ++){
			data[i] = val;
		}
	}
	
	public float[] getUniform(){
		return data;
	}
	
	public Mat4f copy(){
		return new Mat4f(data);
	}
	
	public Mat4f mult(Mat4f other){
		Mat4f result = new Mat4f(0);
		for(int k=0; k<=12; k+=4){
		    for(int i=0; i<4; i++){
		        for (int j=0, bCount=0; j<4; j++, bCount+=4){
		            result.data[k+i] += data[k+j%4] * other.data[bCount+i%4];
		        }
		    }
		}
		return result;
	}
	
	public void print(){
		System.out.println("{");
		for (int i = 0; i < 4; i ++){
			for (int j = 0; j < 4; j ++){
				System.out.print("  " + data[i*4+j] + " ");
			}
			System.out.println();
		}
		System.out.println("}");
	}
}
