package br.usp.icmc.caed.ald.client.data.tmp;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.gwt.core.client.Callback;
import com.google.gwt.json.client.JSONObject;

public class DataStoreTest implements DataStore {

	@SuppressWarnings("unused")
	private static class UUID {
		private static final char[] CHARS =
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".
				toCharArray(); 

		/**
		 * Generate a random uuid of the specified length. Example: uuid(15)
		 * returns "VcydxgltxrVZSTV"
		 * 
		 * @param len
		 *            the desired number of characters
		 */
		public static String uuid(int len) {
			return uuid(len, CHARS.length);
		}

		/**
		 * Generate a random uuid of the specified length, and radix.
		 * Examples:
		 * <ul>
		 * <li>uuid(8, 2) returns "01001010" (8 character ID, base=2)
		 * <li>uuid(8, 10) returns "47473046" (8 character ID, base=10)
		 * <li>uuid(8, 16) returns "098F4D35" (8 character ID, base=16)
		 * </ul>
		 * 
		 * @param len the desired number of characters
		 * @param radix the number of values for each character (be <= 62)
		 */
		public static String uuid(int len, int radix) {
			if (radix > CHARS.length) {
				throw new IllegalArgumentException();
			}
			char[] uuid = new char[len];
			// Compact form
			for (int i = 0; i < len; i++) {
				uuid[i] = CHARS[(int)(Math.random()*radix)];
			}
			return new String(uuid);
		}

		/**
		 * Generate a RFC4122. Example: "92329D39-6F5C-4520-ABFC-AAB64544E172"
		 */
		public static String uuid() {
			char[] uuid = new char[36];
			int r;

			// rfc4122 requires these characters
			uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
			uuid[14] = '4';

			// Fill in random data. Set the rfc4122, sec. 4.1.5
			for (int i = 0; i < 36; i++) {
				if (uuid[i] == 0) {
					r = (int) (Math.random()*16);
					uuid[i] = CHARS[(i == 19) ? (r & 0x3) | 0x8 : r & 0xf];
				}
			}
			return new String(uuid);
		}
	}

	private final static Map<String, Map<String, MetadataModel>>
	domain_elements = new HashMap<String, Map<String,MetadataModel>>() {
		private static final long serialVersionUID = 1L; {

			Map<String, MetadataModel> skills = new HashMap<String, MetadataModel>();
			/*
			skills.put("s1", new MetadataModel().setId("s1").setType(MetadataModel.TYPE.SKILL).setLabel("Skill 1"));
			skills.put("s2", new MetadataModel().setId("s2").setType(MetadataModel.TYPE.SKILL).setLabel("Skill 2"));
			skills.put("s3", new MetadataModel().setId("s3").setType(MetadataModel.TYPE.SKILL).setLabel("Skill 3"));
			skills.put("s4", new MetadataModel().setId("s4").setType(MetadataModel.TYPE.SKILL).setLabel("Skill 4"));
			skills.put("s5", new MetadataModel().setId("s5").setType(MetadataModel.TYPE.SKILL).setLabel("Skill 5"));
			**/
			this.put(MetadataModel.TYPE.SKILL, skills);
			
			/**
			String id = "";
			String label = "";
			String parentId = "";
			Map<String, MetadataModel> knowledges = new HashMap<String, MetadataModel>();
			id = "wkps"; label = "WAY-knowledge of PS";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label));
			parentId = id; id = "pspt"; label = "PS for primary task";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "wkps"; id = "psd"; label = "PS for dialogue";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "ps"; label = "Praise strategy";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "psd"; id = "rs"; label = "Reward strategy";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "rstib"; label = "To increase behavior";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "rstib"; id = "okhrs"; label = "Oinas-Kukkonen & Harjumaa’s reward strategy to increase behavior";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "rstib"; id = "aetalrs"; label = "Aparicio et al.’s reward strategy to increase behavior";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "rstib"; id = "svrs"; label = "Simões & Vilas’s reward strategy to increase behavior";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "rs"; id = "rsrb"; label = "To reduce behavior behavior";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "psd"; id = "sim-s"; label = "Similarity strategy";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "psd"; id = "sugg-s"; label = "Suggestion strategy";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "sugg-s"; id = "sugg-tba"; label = "To be attentive";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "saka-sugg"; label = "Sakamoto et al.'s suggestion strategy to be attentive";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "sugg-tba"; id = "oinas-sugg"; label = "Oinas-Kukkonen & Harjumaa’s suggestion strategy to be attentive";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "sugg-tba"; id = "blohm-sugg"; label = "Blohm & Leimeister’s suggestion strategy to be attentive";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "sugg-s"; id = "ss-affe"; label = "To be affetive";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "wkps"; id = "ps-sc"; label = "PS for system credibility";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "wkps"; id = "ps-ss"; label = "PS for social support";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			this.put(MetadataModel.TYPE.KNOWLEDGE, knowledges);
			*/

			
			String id = "";
			String label = "";
			String parentId = "";
			Map<String, MetadataModel> knowledges = new HashMap<String, MetadataModel>();
			id = "gcls"; label = "Gamified CL scenario for argue, counter-argue and integration";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label));
			parentId = id; id = "gcls1"; label = "Gamified CL scenario 1";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "ls1"; label = "Learning strategy (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "la1"; label = "Learning by argue (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "ar"; label = "Argue role";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "ar-l1"; label = "l1";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "la1"; id = "cr"; label = "Co-argue role";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "cr-l2"; label = "l2";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gcls1"; id = "ls2"; label = "Learning strategy (2)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gcls1"; id = "ms1"; label = "Motivational strategy (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gcls1"; id = "ms2"; label = "Motivational strategy (2)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "gps1"; label = "Gamify by PS COOP (2)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "apr"; label = "Achiever player role";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "apr-l2"; label = "l2";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gps1"; id = "spr"; label = "Socializer player role";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "spr-l1"; label = "l1";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gps1"; id = "i-mg"; label = "I-mot goal";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "i-mg"; id = "i-mg-sm1"; label = "Satisfy mastery (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "i-mg"; id = "ba1"; label = "Being attentive (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "i-mg"; id = "ib1"; label = "Increase behavior (familar behavior) (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gcls1"; id = "gs1"; label = "Gameplay strategy (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "igp1"; label = "I-gameplay (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "wu"; label = "What use";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "cd1"; label = "Comunal discovery (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gcls1"; id = "gs2"; label = "Gameplay strategy (2)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gcls1"; id = "clg"; label = "CL gameplay";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "wg1"; label = "W(A)-gameplay (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "htp"; label = "How to play";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "clgdcd1"; label = "CL game dynamic for communal discovery (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "ni1"; label = "Necessary interaction (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "git1"; label = "Gamified instigating thinking (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "clgdcd1"; id = "ni2"; label = "Necessary interaction (2)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "gid1"; label = "Gamified instigating discussion (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "gie"; label = "Gamified I event";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gid1"; id = "gle"; label = "Gamified L event";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "geo1"; label = "Gamified expose opinion (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "gse"; label = "Game stimulus event";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "scunce1"; label = "Show condition to unlock new content event (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "gse"; id = "scunce2"; label = "Highlight next step event (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "geo1"; id = "gme"; label = "Game mechanics event";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "eoe1"; label = "Expose opinion event (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "pr"; label = "Player role";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "pr-l2"; label = "l2";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "eoe1"; id = "gm"; label = "Game mechanics";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "ca-1"; label = "Counter-argue (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "eoe1"; id = "bftp"; label = "Benefits for the player";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "sm1"; label = "Satisfy mastery (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			
			parentId = "geo1"; id = "gcqe"; label = "Game consequence event";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "sconseq2"; label = "Give points event (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			
			parentId = "clgdcd1"; id = "ni3"; label = "Necessary interaction (3)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "gii1"; label = "Gamified integrating ideas (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = "clgdcd1"; id = "ni4"; label = "Necessary interaction (4)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			parentId = id; id = "gar1"; label = "Gamified affirmative reaction (1)";
			knowledges.put(id, new MetadataModel().setId(id).setType(MetadataModel.TYPE.KNOWLEDGE).setLabel(label).setParentId(parentId));
			this.put(MetadataModel.TYPE.KNOWLEDGE, knowledges);

			/*
			**/
			
			/*
			Map<String, MetadataModel> knowledges =
					new HashMap<String, MetadataModel>();
			knowledges.put("k1", new MetadataModel().setId("k1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 1"));
			knowledges.put("k2", new MetadataModel().setId("k2")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 2"));
			knowledges.put("k2-1", new MetadataModel().setId("k2-1").setParentId("k2")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 2.1"));
			knowledges.put("k2-2", new MetadataModel().setId("k2-2").setParentId("k2")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 2.2"));
			knowledges.put("k3", new MetadataModel().setId("k3")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 3"));
			knowledges.put("k4", new MetadataModel().setId("k4")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 4"));
			knowledges.put("k5", new MetadataModel().setId("k5")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5"));
			knowledges.put("k5-1", new MetadataModel().setId("k5-1").setParentId("k5")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1"));
			knowledges.put("k5-1-1", new MetadataModel().setId("k5-1-1").setParentId("k5-1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.1"));
			knowledges.put("k5-1-2", new MetadataModel().setId("k5-1-2").setParentId("k5-1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.2"));
			knowledges.put("k5-1-3", new MetadataModel().setId("k5-1-3").setParentId("k5-1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.3"));
			knowledges.put("k5-1-4", new MetadataModel().setId("k5-1-4").setParentId("k5-1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4"));
			knowledges.put("k5-1-4-1", new MetadataModel().setId("k5-1-4-1").setParentId("k5-1-4")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4.1"));
			knowledges.put("k5-1-4-1-1", new MetadataModel().setId("k5-1-4-1-1").setParentId("k5-1-4-1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4.1.1"));
			knowledges.put("k5-1-4-1-2", new MetadataModel().setId("k5-1-4-1-2").setParentId("k5-1-4-1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4.1.2"));
			knowledges.put("k5-1-4-1-3", new MetadataModel().setId("k5-1-4-1-3").setParentId("k5-1-4-1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4.1.3"));
			knowledges.put("k5-1-4-1-4", new MetadataModel().setId("k5-1-4-1-4").setParentId("k5-1-4-1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4.1.4"));
			knowledges.put("k5-1-4-2", new MetadataModel().setId("k5-1-4-2").setParentId("k5-1-4")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4.2"));
			knowledges.put("k5-1-4-2-A", new MetadataModel().setId("k5-1-4-2-A").setParentId("k5-1-4-2")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4.2-A"));
			knowledges.put("k5-1-4-2-B", new MetadataModel().setId("k5-1-4-2-B").setParentId("k5-1-4-2")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4.2-B"));
			knowledges.put("k5-1-4-2-C", new MetadataModel().setId("k5-1-4-2-C").setParentId("k5-1-4-2")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.4.2-C"));
			knowledges.put("k5-1-5", new MetadataModel().setId("k5-1-5").setParentId("k5-1")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 5.1.5"));
			knowledges.put("k6", new MetadataModel().setId("k6")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 6"));
			knowledges.put("k7", new MetadataModel().setId("k7")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 7"));
			knowledges.put("k8", new MetadataModel().setId("k8")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 8"));
			knowledges.put("k9", new MetadataModel().setId("k9")
					.setType(MetadataModel.TYPE.KNOWLEDGE).setLabel("Knowledge 9"));
			this.put(MetadataModel.TYPE.KNOWLEDGE, knowledges);
			*/

			Map<String, MetadataModel> competencies = new HashMap<String, MetadataModel>();
			/*
			competencies.put("c1", new MetadataModel().setId("c1").setType(MetadataModel.TYPE.COMPETENCY).setLabel("Competency 1").addProperty(new PropertyModel("hasSkill", skills.get("s1"))).addProperty(new PropertyModel("hasKnowledge", knowledges.get("k1"))));
			competencies.put("c2", new MetadataModel().setId("c2").setType(MetadataModel.TYPE.COMPETENCY).setLabel("Competency 2").addProperty(new PropertyModel("hasSkill", skills.get("s2"))).addProperty(new PropertyModel("hasKnowledge", knowledges.get("k3"))));
			competencies.put("c3", new MetadataModel().setId("c3").setType(MetadataModel.TYPE.COMPETENCY).setLabel("Competency 3").addProperty(new PropertyModel("hasSkill", skills.get("s3"))).addProperty(new PropertyModel("hasKnowledge", knowledges.get("k9"))));
			**/
			this.put(MetadataModel.TYPE.COMPETENCY, competencies);

			Map<String, MetadataModel> learningMaterials = new HashMap<String, MetadataModel>();
			/*
			learningMaterials.put("lm1", new MetadataModel().setId("lm1").setType(MetadataModel.TYPE.LEARNING_MATERIAL).setLabel("Learning Material 1").addProperty(new PropertyModel("hasPrerequiste", competencies.get("c1"), "s1k1")).addProperty(new PropertyModel("hasLearningObjective", competencies.get("c3"), "s3k3")).addProperty(new PropertyModel("hasLearningObjective", competencies.get("c2"), "s2k2")).addProperty(new PropertyModel("hasLearningObjective", competencies.get("c2"), "s1k1")));
			**/
			this.put(MetadataModel.TYPE.LEARNING_MATERIAL, learningMaterials);

			Map<String, MetadataModel> uols = new HashMap<String, MetadataModel>();
			/*
			uols.put("u1", new MetadataModel().setId("u1").setType(MetadataModel.TYPE.UoL).setLabel("UoL 1"));
			uols.put("u2", new MetadataModel().setId("u2").setType(MetadataModel.TYPE.UoL).setLabel("UoL 2"));
			uols.put("u3", new MetadataModel().setId("u3").setType(MetadataModel.TYPE.UoL).setLabel("UoL 3"));
			**/
			this.put(MetadataModel.TYPE.UoL, uols);

		}
	};

	private final static Map<String, MetadataModel> learners = new HashMap<String, MetadataModel>(){
		private static final long serialVersionUID = 1L; {
			MetadataModel user = null;
			user = new MetadataModel().setId("l1").setLabel("learner1")
					.setType(MetadataModel.TYPE.LEARNER).addProperty(
							new PropertyModel("hasEmail", ("student1@ime.usp.br")));
			this.put(user.getId(), user);
			user = new MetadataModel().setId("l2").setLabel("learner2")
					.setType(MetadataModel.TYPE.LEARNER).addProperty(
							new PropertyModel("hasEmail", ("student2@ime.usp.br")));
			this.put(user.getId(), user);
			user = new MetadataModel().setId("l3").setLabel("learner3")
					.setType(MetadataModel.TYPE.LEARNER).addProperty(
							new PropertyModel("hasEmail", "student3@ime.usp.br"))
							.addProperty(new PropertyModel("hasHomepage", "http://www.ime.usp.br/~student3"));
			this.put(user.getId(), user);
			user = new MetadataModel().setId("l4").setLabel("learner4")
					.setType(MetadataModel.TYPE.LEARNER).addProperty(
							new PropertyModel("hasEmail", ("student4@ime.usp.br")));
			this.put(user.getId(), user);
			user = new MetadataModel().setId("l5").setLabel("learner5")
					.setType(MetadataModel.TYPE.LEARNER).addProperty(
							new PropertyModel("hasEmail", ("student5@ime.usp.br")));
			this.put(user.getId(), user);
			user = new MetadataModel().setId("l6").setLabel("learner6")
					.setType(MetadataModel.TYPE.LEARNER).addProperty(
							new PropertyModel("hasEmail", ("student6@ime.usp.br")))
							.setProperties(Arrays.asList(new PropertyModel[] {
									new PropertyModel("hasCollaborativeExperience", "low"), }));
			this.put(user.getId(), user);
			user = new MetadataModel().setId("l7").setLabel("learner7")
					.setType(MetadataModel.TYPE.LEARNER).addProperty(
							new PropertyModel("hasEmail", ("student7@ime.usp.br")));
			this.put(user.getId(), user);
			user = new MetadataModel().setId("l8").setLabel("learner8")
					.setType(MetadataModel.TYPE.LEARNER).addProperty(
							new PropertyModel("hasEmail", ("student8@ime.usp.br")));
			this.put(user.getId(), user);
			user = new MetadataModel().setId("l9").setLabel("learner9")
					.setType(MetadataModel.TYPE.LEARNER).addProperty(
							new PropertyModel("hasEmail", ("student9@ime.usp.br")));
			this.put(user.getId(), user);
		}
	};

	public final static DAO<String, MetadataModel> learnerModelDAO =
			new DAO<String, MetadataModel>() {
		@Override
		public void create(MetadataModel model,
				Callback<MetadataModel, Throwable> callback) {
			model.setId("l"+UUID.uuid(3));
			DataStoreTest.learners.put(model.getId(), model);
			callback.onSuccess(model); 
		}
		@Override
		public void retrieveById(String id,
				Callback<MetadataModel, Throwable> callback) {
			MetadataModel toReturn = DataStoreTest.learners.get(id);
			callback.onSuccess(toReturn);
		}

		@Override
		public void update(MetadataModel model,
				Callback<MetadataModel, Throwable> callback) {
			DataStoreTest.learners.put(model.getId(), model);
			callback.onSuccess(model);
		}

		@Override
		public void deleteById(String id,
				Callback<MetadataModel, Throwable> callback) {
			MetadataModel user = DataStoreTest.learners.get(id);
			DataStoreTest.learners.remove(id);
			callback.onSuccess(user);
		}

		@Override
		public void retrieve(Callback<List<MetadataModel>, Throwable> callback) {
			callback.onSuccess(new ArrayList<MetadataModel>(DataStoreTest.learners.values()));
		}

		@Override
		public void retrieve(MetadataModel toFind,
				Callback<List<MetadataModel>, Throwable> callback) {
			List<MetadataModel> result = new ArrayList<MetadataModel>();
			for (MetadataModel user : DataStoreTest.learners.values()) {
				if (toFind.getType()!=null && toFind.getType().equals(user.getType())) {
					result.add(user);
				}
			}
			callback.onSuccess(result);
		}

		@Override
		public void delete(MetadataModel toFind,
				Callback<List<MetadataModel>, Throwable> callback) {
			List<MetadataModel> toReturn = new ArrayList<MetadataModel>();
			if (toFind.getId()!=null) {
				toReturn.add(DataStoreTest.learners.get(toFind.getId()));
				DataStoreTest.learners.remove(toFind.getId());
			}
			//TODO Continue with others else-if (model.getId==null)
			callback.onSuccess(toReturn);
		}
	};

	public final static DAO<String, MetadataModel> domainModelDAO =
			new DAO<String, MetadataModel>() {

		@Override
		public void create(MetadataModel model, Callback<MetadataModel, Throwable> callback) {
			model.setId("m"+model.getType()+UUID.uuid(5));
			DataStoreTest.domain_elements.get(model.getType()).put(model.getId(), model);
			callback.onSuccess(model);
		}

		@Override
		public void retrieve(Callback<List<MetadataModel>, Throwable> callback) {
			List<MetadataModel> result = new ArrayList<MetadataModel>();
			for (Map<String, MetadataModel> metadatas : DataStoreTest.domain_elements.values()) {
				result.addAll(metadatas.values());
			}
			callback.onSuccess(result);
		}

		@Override
		public void retrieve(MetadataModel toFind,
				Callback<List<MetadataModel>, Throwable> callback) {
			List<MetadataModel> result = new ArrayList<MetadataModel>();
			for (MetadataModel model : domain_elements.get(toFind.getType()).values()) {
				if (toFind.getParentId()==null && model.getParentId()==null) {
					result.add(model);
				} else if (toFind.getParentId().equals(model.getParentId())) {
					result.add(model);
				}
			}
			callback.onSuccess(result);
		}

		@Override
		public void retrieveById(String id, Callback<MetadataModel, Throwable> callback) {
			MetadataModel result = null;
			for (Map<String, MetadataModel> _metadatas : domain_elements.values()) {
				if (_metadatas.containsKey(id)) {
					result = _metadatas.get(id);
				}
			}
			callback.onSuccess(result);
		}

		@Override
		public void update(MetadataModel model,
				Callback<MetadataModel, Throwable> callback) {
			if (domain_elements.containsKey(model.getType())) {
				Map<String, MetadataModel> _metadatas = domain_elements.get(model.getType());
				_metadatas.put(model.getId(), model);
				domain_elements.put(model.getType(), _metadatas);
			}
		}

		@Override
		public void delete(MetadataModel toFind,
				Callback<List<MetadataModel>, Throwable> callback) {
			// TODO Auto-generated method stub
		}

		@Override
		public void deleteById(String id, Callback<MetadataModel, Throwable> callback) {
			MetadataModel result = null;
			for (String elementType : domain_elements.keySet()) {
				Map<String, MetadataModel> _metadatas = domain_elements.get(elementType);
				if (_metadatas.containsKey(id)) {
					result = _metadatas.get(id);
					_metadatas.remove(id);
					domain_elements.put(elementType, _metadatas);
					break;
				}
			}
			callback.onSuccess(result);
		}

	};

	@Override
	public DAO<String, MetadataModel> getLearnerModelDAO() {
		return DataStoreTest.learnerModelDAO;
	}

	@Override
	public DAO<String, MetadataModel> getDomainModelDAO() {
		return DataStoreTest.domainModelDAO;
	}

	@Override
	public DAO<String, JSONObject> getOntologyDAO() {
		return null;
	}

}
