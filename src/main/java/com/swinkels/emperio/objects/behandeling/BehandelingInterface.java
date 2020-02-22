package com.swinkels.emperio.objects.behandeling;

public interface BehandelingInterface {
	public void save();

	public Behandeling getById(int id); // deze is static

	public void delete();

	public void update();

	public void changeStatus();

}
