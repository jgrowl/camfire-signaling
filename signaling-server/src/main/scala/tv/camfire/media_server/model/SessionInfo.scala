package tv.camfire.media_server.model

/**
 * User: jonathan
 * Date: 7/12/13
 * Time: 1:26 PM
 */
class SessionInfo() {
  var id: java.lang.Long = null
  var sessionId: String = null
  var stream: StreamInfo = null
}


//@Model
//class User {
//  @Id
//  private Long id;
//  @Attribute
//  private String name;
//  @Attribute
//  @Indexed
//  private int age;
//  @Reference
//  @Indexed
//  private Country country;
//  @CollectionList(of = Comment.class)
//  @Indexed
//  private List<Comment> comments;
//  @CollectionSet(of = Item.class)
//  @Indexed
//  private Set<Item> purchases;
//  @CollectionMap(key = Integer.class, value = Item.class)
//  @Indexed
//  private Map<Integer, Item> favoritePurchases;
//  @CollectionSortedSet(of = Item.class, by = "price")
//  @Indexed
//  private Set<Item> orderedPurchases;
//  @Array(of = Item.class, length = 3)
//  @Indexed
//  private Item[] threeLatestPurchases;
//}
