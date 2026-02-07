import tkinter as tk


class ScrollBar(tk.Frame):
    """Cette classe crée un conteneur qui gère automatiquement l'apparition d'une
    barre de défilement et la prise en charge de la molette de la souris.
    Tout widget ajouté à 'self.content' sera inclus dans la zone défilable.

    Attributs:
        canvas (tk.Canvas): La zone de dessin servant de fenêtre de vue (viewport).
        scrollbar (tk.Scrollbar): Le widget de contrôle du défilement.
        content (tk.Frame): Le cadre intérieur réel où les widgets doivent être placés.
    """

    def __init__(self, parent, width, bg):
        super().__init__(parent, bg=bg, width=width)
        self.pack_propagate(False)

        self.canvas = tk.Canvas(self, bg=bg, highlightthickness=0, width=width)
        self.scrollbar = tk.Scrollbar(self, orient="vertical", command=self.canvas.yview)

        self.content = tk.Frame(self.canvas, bg=bg)

        self.content.bind(
            "<Configure>",
            lambda e: self.canvas.configure(scrollregion=self.canvas.bbox("all"))
        )

        self.canvas.create_window((width // 2, 0), window=self.content, anchor="n", width=width)
        self.canvas.configure(yscrollcommand=self.scrollbar.set)

        self.canvas.pack(side="left", fill="both", expand=True)
        self.scrollbar.pack(side="right", fill="y")

        self.canvas.bind("<Enter>", self.bind_mousewheel)
        self.canvas.bind("<Leave>", self.unbind_mousewheel)

    # ---- SCROLL ----

    def on_scroll(self, event):
        """Gère le défilement via la molette (Windows/MacOS)."""
        self.canvas.yview_scroll(int(-1 * (event.delta / 120)), "units")

    def scroll_haut(self, event):
        """Fait défiler la vue vers le haut (Linux/Unix)."""
        self.canvas.yview_scroll(-1, "units")

    def scroll_bas(self, event):
        """Fait défiler la vue vers le bas (Linux/Unix)."""
        self.canvas.yview_scroll(1, "units")

    def bind_mousewheel(self, event):
        """Lie les événements de la souris à l'application entière quand le curseur entre."""
        self.canvas.bind_all("<MouseWheel>", self.on_scroll)
        self.canvas.bind_all("<Button-4>", self.scroll_haut)
        self.canvas.bind_all("<Button-5>", self.scroll_haut)

    def unbind_mousewheel(self, event):
        """Délie les événements de la souris quand le curseur quitte la zone."""
        self.canvas.unbind_all("<MouseWheel>")
        self.canvas.unbind_all("<Button-4>")
        self.canvas.unbind_all("<Button-5>")
