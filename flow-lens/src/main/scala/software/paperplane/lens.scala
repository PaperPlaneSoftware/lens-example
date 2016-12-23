package software.paperplane

object lens {
  case class Lens[T, U](get: T => U, set: (T, U) => T) {
    def compose[V](next: Lens[U, V]): Lens[T, V] = Lens(
      get = (t: T) => next.get(get(t)),
      set = (t: T, v: V) => set(t, next.set(get(t), v))
    )
  }
}
