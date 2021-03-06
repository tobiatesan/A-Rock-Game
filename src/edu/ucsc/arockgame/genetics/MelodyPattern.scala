package edu.ucsc.arockgame.genetics
import edu.ucsc.arockgame.genetics._
import javax.sound.midi._
import java.util.Random

object MelodyPattern extends Phenotype {
	val random = new Random
	
	val PENTATONIC = Array(
			0, 2, 4, 7, 9,
			12, 14, 16, 19, 21,
			24, 26, 28, 31, 33, 36
	)
	
	def buildTrack(genotype: Genotype, track: Track, displacement: Int) {
		var message: ShortMessage = new ShortMessage
		message.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 1, 0)
		track.add(new MidiEvent(message, 0))
		val dna = genotype.dna
		var previous = -1
		for (i <- 0 until 32) {
			val j = i * 4
			var event: MidiEvent = null
			val index = (if (dna contains j) 8 else 0) + (if (dna contains j+1) 4 else 0) +
					(if (dna contains j+2) 2 else 0) + (if (dna contains j+3) 1 else 0)
			val note = PENTATONIC(index) + getNote("G3")
			
			if (index != 14) { // not pause
				message = new ShortMessage
				message.setMessage(ShortMessage.NOTE_ON, 0, note, 127)
				event = new MidiEvent(message, i*16+ displacement)
				track.add(event)
			}
			
			if (index != 15 && previous != -1) { // not hold
				message = new ShortMessage
				message.setMessage(ShortMessage.NOTE_OFF, 0, note, 127)
				event = new MidiEvent(message, (i+1)*16+ displacement)
				track.add(event)
			}
		}
	}

}