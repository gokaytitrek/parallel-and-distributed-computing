/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorClock;

/**
 * Enumerates the four different outcomes of comparing two VectorClocks.
 * 
 * @author Frits de Nijs
 * @author Peter Dijkshoorn
 */
public enum VectorComparison
{
	GREATER, EQUAL, SMALLER, SIMULTANEOUS
}